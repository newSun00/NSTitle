# NSTitle

NSTitle 1.0.0

+ EVENT 추가
+ EquipTitleEvent
    - 플레이어가 칭호를 착용할 때
+ UnEquipTitleEvent
    - 플레아어가 착용된 칭호를 뺄 때 ( 시스템적으로 빼는 건 인식하지 않습니다. )


Gradle

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        implementation 'com.github.newSun00:NSTitle:Tag'
	}

Maven

Add to pom.xml

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
 
Step 2. Add the dependency

	<dependency>
	    <groupId>com.github.newSun00</groupId>
	    <artifactId>NSTitle</artifactId>
	    <version>Tag</version>
	</dependency>
