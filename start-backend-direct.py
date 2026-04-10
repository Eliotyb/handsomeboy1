import os
import subprocess
import glob

# Configuration
backend_dir = r"C:\Users\杨彬\Desktop\社团\backend"
java_home = r"C:\Program Files\Java\jdk1.8.0_144"
maven_repo = r"C:\Users\杨彬\.m2\repository"

print("=" * 60)
print("Starting backend compilation and execution")
print("=" * 60)

# Find all JAR dependencies
print("\nCollecting JAR files from Maven repository...")
jars = []
for root, dirs, files in os.walk(maven_repo):
    for file in files:
        if file.endswith('.jar'):
            jars.append(os.path.join(root, file))

print(f"Found {len(jars)} JAR files")

# Build classpath
classpath = ';'.join(jars)
classes_dir = os.path.join(backend_dir, 'target', 'classes')

# Ensure classes directory exists
os.makedirs(classes_dir, exist_ok=True)

# Compile Java files
print("\nCompiling Java sources...")
javac = os.path.join(java_home, 'bin', 'javac.exe')
java_sources = []

# Find all Java source files
for root, dirs, files in os.walk(os.path.join(backend_dir, 'src', 'main', 'java')):
    for file in files:
        if file.endswith('.java'):
            java_sources.append(os.path.join(root, file))

print(f"Found {len(java_sources)} Java source files")

if java_sources:
    # Compile with javac
    cmd = [javac, '-cp', classpath, '-d', classes_dir] + java_sources
    result = subprocess.run(cmd, capture_output=True, text=True)
    
    if result.returncode != 0:
        print("Compilation failed:")
        print(result.stderr)
    else:
        print("Compilation successful!")

# Run the application
print("\nStarting Spring Boot application...")
java_exe = os.path.join(java_home, 'bin', 'java.exe')

# Use a response file to avoid command line length limits
args_file = os.path.join(backend_dir, 'java.args')
with open(args_file, 'w') as f:
    f.write(f"-cp\n{classpath}\ncom.community.groupon.GrouponApplication")

subprocess.run([java_exe, f"@{args_file}"])
