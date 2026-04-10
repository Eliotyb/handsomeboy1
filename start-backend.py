import os
import subprocess
import glob

backend_dir = r"C:\Users\杨彬\Desktop\社团\backend"
maven_repo = r"C:\Users\杨彬\.m2\repository"

# Build classpath
classpath = os.path.join(backend_dir, "target", "classes")

# Find all JARs in Maven repository
for jar_path in glob.glob(os.path.join(maven_repo, "**", "*.jar"), recursive=True):
    classpath += ";" + jar_path

print("Starting Spring Boot application...")
print("Classpath length:", len(classpath))

# Run Spring Boot application
os.chdir(backend_dir)
subprocess.run([
    "java",
    "-cp",
    classpath,
    "com.community.groupon.GrouponApplication"
])
