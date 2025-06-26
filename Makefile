clean:
	./gradlew clean

test:
	./gradlew test

reload-classes:
	./gradlew -t classes

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

lint:
	./gradlew checkstyleMain checkstyleTest

help:
	@echo "make start-prod      	- запустить Spring Boot в продакшн-профиле"
	@echo "make install         	- собрать самодостаточный дистрибутив"
	@echo "make clean   		- удалить артефакты сборки"
	@echo "make test    		- выполнить все тесты"
	@echo "make lint            	- выполнить проверку кода через Checkstyle"
	@echo "make reload-classes  	- авто-пересборка классов при изменениях"

.PHONY: test clean reload-classes start-prod install lint help
