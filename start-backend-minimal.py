import os
import subprocess

# Configuration
backend_dir = r"C:\Users\杨彬\Desktop\社团\backend"
java_home = r"C:\Program Files\Java\jdk1.8.0_144"
maven_repo = r"C:\Users\杨彬\.m2\repository"

print("=" * 60)
print("Starting backend with minimal classpath")
print("=" * 60)

# Required dependencies (minimal set for Spring Boot)
required_jars = []

# Function to find JAR in maven repo
def find_jar(group_id, artifact_id, version=None):
    group_path = group_id.replace('.', os.sep)
    artifact_path = artifact_id.replace('.', os.sep)
    
    search_paths = [
        os.path.join(maven_repo, group_path, artifact_id),
        os.path.join(maven_repo, group_path, artifact_path),
    ]
    
    for search_path in search_paths:
        if os.path.exists(search_path):
            # Find version directory
            for version_dir in os.listdir(search_path):
                version_path = os.path.join(search_path, version_dir)
                if os.path.isdir(version_path):
                    # If version specified, check it
                    if version and version_dir != version:
                        continue
                    # Find JAR file
                    for f in os.listdir(version_path):
                        if f.endswith('.jar'):
                            required_jars.append(os.path.join(version_path, f))
                            return True
    return False

# Add essential dependencies
print("\nAdding essential dependencies...")
find_jar('org.springframework.boot', 'spring-boot', '2.7.15')
find_jar('org.springframework.boot', 'spring-boot-autoconfigure', '2.7.15')
find_jar('org.springframework.boot', 'spring-boot-starter', '2.7.15')
find_jar('org.springframework.boot', 'spring-boot-starter-web', '2.7.15')
find_jar('org.springframework.boot', 'spring-boot-starter-data-redis', '2.7.15')
find_jar('org.springframework.boot', 'spring-boot-starter-security', '2.7.15')
find_jar('com.baomidou', 'mybatis-plus-boot-starter', '3.5.3')
find_jar('mysql', 'mysql-connector-java', '8.0.33')
find_jar('io.jsonwebtoken', 'jjwt', '0.9.1')
find_jar('org.projectlombok', 'lombok')

# Add all Spring framework JARs
spring_dir = os.path.join(maven_repo, 'org', 'springframework')
for root, dirs, files in os.walk(spring_dir):
    for file in files:
        if file.endswith('.jar'):
            required_jars.append(os.path.join(root, file))

# Add other required dependencies
other_deps = ['org.slf4j', 'ch.qos.logback', 'com.fasterxml.jackson', 'io.lettuce']
for dep_group in other_deps:
    dep_dir = os.path.join(maven_repo, dep_group.replace('.', os.sep))
    if os.path.exists(dep_dir):
        for root, dirs, files in os.walk(dep_dir):
            for file in files:
                if file.endswith('.jar'):
                    required_jars.append(os.path.join(root, file))

print(f"Added {len(required_jars)} essential JAR files")

# Build classpath
classes_dir = os.path.join(backend_dir, 'target', 'classes')
classpath = classes_dir + ';' + ';'.join(required_jars)

print(f"\nClasspath length: {len(classpath)} characters")

# Compile first if needed
if not os.path.exists(os.path.join(classes_dir, 'com', 'community', 'groupon', 'GrouponApplication.class')):
    print("\nCompiling Java sources...")
    javac = os.path.join(java_home, 'bin', 'javac.exe')
    java_sources = []
    
    # Find all Java source files
    src_dir = os.path.join(backend_dir, 'src', 'main', 'java', 'com', 'community', 'groupon')
    for f in os.listdir(src_dir):
        if f.endswith('.java'):
            java_sources.append(os.path.join(src_dir, f))
    
    # Also find subdirectory sources
    for subdir in ['config', 'controller', 'entity', 'repository', 'service']:
        subdir_path = os.path.join(src_dir, subdir)
        if os.path.exists(subdir_path):
            for f in os.listdir(subdir_path):
                if f.endswith('.java'):
                    java_sources.append(os.path.join(subdir_path, f))
    
    print(f"Found {len(java_sources)} Java source files")
    
    # Compile
    cmd = [javac, '-cp', classpath, '-d', classes_dir] + java_sources
    result = subprocess.run(cmd, capture_output=True)
    
    if result.returncode != 0:
        print("Compilation failed:")
        print(result.stderr.decode('gbk', errors='ignore'))
        print("Attempting to continue anyway...")
    else:
        print("Compilation successful!")

# Run the application
print("\nStarting Spring Boot application...")
java_exe = os.path.join(java_home, 'bin', 'java.exe')
os.chdir(backend_dir)

try:
    subprocess.run([java_exe, '-cp', classpath, 'com.community.groupon.GrouponApplication'])
except Exception as e:
    print(f"Error: {e}")
