@echo off

SET _JAVA_OPTIONS=-Xmx4096m
SET MKL_PATH=C:\dev\tools\intel\compilers_and_libraries_2019\windows\mkl\bin
CALL "%MKL_PATH%\mklvars.bat" intel64
lein repl
