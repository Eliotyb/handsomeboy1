import os
import subprocess
import glob

backend_dir = r"C:\Users\杨彬\Desktop\社团\backend"
maven_repo = r"C:\Users\杨彬\.m2\repository"
java_home = r"C:\Program Files\Java\jdk1.8.0_144"

print("Building classpath...")
classpath = os.path.join(backend_dir, "target", "classes")

# Add all JARs from Maven repository
jar_count = 0
for jar_path in glob.glob(os.path.join(maven_repo, "**", "*.jar"), recursive=True):
    classpath += ";" + jar_path
    jar_count += 1

print(f"Added {jar_count} JAR files to classpath")
print("Starting Spring Boot application...")

os.chdir(backend_dir)
java_exe = os.path.join(java_home, "bin", "java.exe")
subprocess.run([
    java_exe,
    "-cp",
    classpath,
    "com.community.groupon.GrouponApplication"
])
