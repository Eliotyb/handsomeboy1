import os
import subprocess
import sys

backend_dir = r"C:\Users\杨彬\Desktop\社团\backend"
java_home = r"C:\Program Files\Java\jdk1.8.0_144"
maven_repo = os.path.expanduser(r"~\.m2\repository")

print("=" * 60)
print("Starting Spring Boot Backend")
print("=" * 60)

# Essential JAR paths (using shorter paths)
essential_jars = []

def add_jar(path):
    full_path = os.path.join(maven_repo, path)
    if os.path.exists(full_path):
        essential_jars.append(full_path)
        return True
    print(f"Warning: {path} not found")
    return False

# Add Spring Boot core JARs
print("\nLoading Spring Boot dependencies...")
add_jar(r"org\springframework\boot\spring-boot\2.7.15\spring-boot-2.7.15.jar")
add_jar(r"org\springframework\boot\spring-boot-autoconfigure\2.7.15\spring-boot-autoconfigure-2.7.15.jar")
add_jar(r"org\springframework\boot\spring-boot-starter\2.7.15\spring-boot-starter-2.7.15.jar")
add_jar(r"org\springframework\boot\spring-boot-starter-web\2.7.15\spring-boot-starter-web-2.7.15.jar")
add_jar(r"org\springframework\boot\spring-boot-starter-data-redis\2.7.15\spring-boot-starter-data-redis-2.7.15.jar")
add_jar(r"org\springframework\boot\spring-boot-starter-security\2.7.15\spring-boot-starter-security-2.7.15.jar")

# Add MyBatis Plus
add_jar(r"com\baidu\mybatis-plus\mybatis-plus-boot-starter\3.5.3\mybatis-plus-boot-starter-3.5.3.jar")

# Add MySQL
add_jar(r"mysql\mysql-connector-java\8.0.33\mysql-connector-java-8.0.33.jar")

# Add JWT
add_jar(r"io\jsonwebtoken\jjwt\0.9.1\jjwt-0.9.1.jar")

# Add all Spring Framework JARs recursively
def add_all_jars(directory):
    jars = []
    if os.path.exists(directory):
        for root, dirs, files in os.walk(directory):
            for file in files:
                if file.endswith('.jar'):
                    jars.append(os.path.join(root, file))
    return jars

print("Loading Spring Framework JARs...")
spring_dir = os.path.join(maven_repo, r"org\springframework")
essential_jars.extend(add_all_jars(spring_dir))

print("Loading other dependencies...")
for dep in [r"org\slf4j", r"ch\qos\logback", r"com\fasterxml\jackson", r"io\lettuce", r"redis\clients"]:
    dep_dir = os.path.join(maven_repo, dep)
    essential_jars.extend(add_all_jars(dep_dir))

print(f"Total JARs loaded: {len(essential_jars)}")

# Build classpath
classes_dir = os.path.join(backend_dir, "target", "classes")
classpath = classes_dir + ";" + ";".join(essential_jars)

print(f"\nClasspath length: {len(classpath)} chars")

# Check if already compiled
main_class = os.path.join(classes_dir, "com", "community", "groupon", "GrouponApplication.class")
if not os.path.exists(main_class):
    print("\nNeed to compile first, but compilation requires too many dependencies.")
    print("Please use an IDE like IntelliJ IDEA or Eclipse to run the backend.")
    sys.exit(1)

# Run
print("\nStarting application...")
java_exe = os.path.join(java_home, "bin", "java.exe")
os.chdir(backend_dir)

try:
    subprocess.run([java_exe, "-cp", classpath, "com.community.groupon.GrouponApplication"])
except KeyboardInterrupt:
    print("\nApplication stopped")
except Exception as e:
    print(f"Error: {e}")
