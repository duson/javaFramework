@echo off

:同目录下放置：mybatis-generator-core-1.3.2.jar、mapper-3.3.8.jar、扩展Mapper的Jar包
java -cp ./* org.mybatis.generator.api.ShellRunner -configfile generatorConfig.xml -overwrite

pause;