# MDM

This project is about to implement the functions of MDM (Mobile Device Managemnt) and use the UI/UX of ROCAF.


## Getting Started

Because you should compile the whole project on your own; thus you should install git, JDK 8 and Android Studio.

### Prerequisites

```
OS: Windows or Linux
Software: git, JDK 8 and Android Studio
```

And ensure that your Android Studio works.

### Installing

First, on your workspace git clone the project

```
$ cd $WORKSPACE
$ git clone https://github.com/leviliang/MDM.git
```

Then, open the project on your Android Studio and compile it. 
The following is some gradle info that you may need: 

```
build.gradle (Module: app) 
  android:
    compileSdkVersion 28
    minSdkVersion 17
    targetSdkVersion 26
    versionCode 3
    versionName "1.0.3"
  dependencies:
    api 'com.android.support:appcompat-v7:28.0.0'
```

## Contributing

When contributing to this repository, please first discuss the change you wish to make via issue,
email, or any other method with the owners of this repository before making a change. 

Please note we have a code of conduct, please follow it in all your interactions with the project.

### Pull Request Process

1. Ensure any install or build dependencies are removed before the end of the layer when doing a 
   build.
2. Update the README.md with details of changes to the interface, this includes new environment 
   variables, exposed ports, useful file locations and container parameters.
3. Increase the version numbers in any examples files and the README.md to the new version that this
   Pull Request would represent. The versioning scheme we use is [SemVer](http://semver.org/).
4. You may merge the Pull Request in once you have the sign-off of two other developers, or if you 
   do not have permission to do that, you may request the second reviewer to merge it for you.

### Code of Conduct
Please refer to the [Contributor Covenant][homepage], version 1.4,
available at [http://contributor-covenant.org/version/1/4][version]

[homepage]: http://contributor-covenant.org
[version]: http://contributor-covenant.org/version/1/4/

## Authors

* **Jih Wei Liang** - *Initial work* - [leviliang](https://github.com/leviliang)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
