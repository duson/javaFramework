@echo off

java -cp ./* org.mybatis.generator.api.ShellRunner -configfile generatorConfig.xml -overwrite

pause;