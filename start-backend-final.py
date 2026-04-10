import os
import subprocess
import glob
from pathlib import Path

backend_dir = Path(r"C:\Users\杨彬\Desktop\社团\backend")
maven_repo = Path(r"C:\Users\杨彬\.m2\repository")

# Get all required JAR paths
jar_paths = []

# Essential Spring Boot dependencies
essential_deps = [
    "org/springframework/boot/spring-boot",
    "org/springframework/boot/spring-boot-autoconfigure",
    "org/springframework/boot/spring-boot-starter",
    "org/springframework/boot/spring-boot-starter-web",
    "org/springframework",
    "com/baomidou/mybatis-plus",
    "mysql/mysql-connector-java",
    "org/slf4j",
    "ch/qos/logback",
]

print("Building classpath...")
classpath = str(backend_dir / "target" / "classes")

# Add all JARs from maven repo
for jar in maven_repo.rglob("*.jar"):
    classpath += ";" + str(jar)

print(f"Classpath length: {len(classpath)}")
print("Starting application...")

try:
    # Change to backend directory
    os.chdir(backend_dir)
    
    # Run with shorter command using response file
    args_file = backend_dir / "java.args"
    args_file.write_text(f"-cp\n{classpath}\ncom.community.groupon.GrouponApplication")
    
    subprocess.run(["java", f"@{args_file}"], shell=True)
except Exception as e:
    print(f"Error: {e}")
