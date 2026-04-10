import os
import subprocess

backend_dir = r"C:\Users\杨彬\Desktop\社团\backend"
java_home = r"C:\Program Files\Java\jdk1.8.0_144"
maven_repo = r"C:\Users\杨彬\.m2\repository"

# Find all JAR dependencies
jars = []
for root, dirs, files in os.walk(maven_repo):
    for file in files:
        if file.endswith('.jar'):
            jars.append(os.path.join(root, file))

classpath = ';'.join(jars)

# Compile
print("Compiling...")
javac = os.path.join(java_home, 'bin', 'javac.exe')
subprocess.run([
    javac,
    '-cp', classpath,
    '-d', os.path.join(backend_dir, 'target', 'classes'),
    os.path.join(backend_dir, 'src', 'main', 'java', 'com', 'community', 'groupon', '*.java')
], shell=True)

print("Done!")
