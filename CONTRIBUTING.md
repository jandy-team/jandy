# Contributing

We love pull requests from everyone.

Install tools for developing: 
- Python2 & pip
- Nodejs
- Ruby (for saas)
- Maven 3.x
- JDK 1.8
- Thrift v0.9.x

Fork, then clone the repo:

    git clone https://github.com/jcooky/jandy.git

Make sure the tests pass:

    mvn package

Execute jandy-server in your local-machine:

    java -jar jandy-server/target/jandy-server-{version}.jar

Access local page of jandy:

    http://localhost:3000

Push to your fork and submit a pull request.<br>
Some things that will increase the chance that your pull request is accepted:

- Write tests.
- Follow [Google style guide](https://google.github.io/styleguide/javaguide.html).
- Write a [good commit message](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html).

## Running profiles on local machine instead of Travis-CI

1. Access <code>localhost:3000</code>, and then sign in jandy by GitHub OAuth.
2. Write <code>.jandy.yml</code> in target repository.
  - See [Jandy User Guide](http://github.com/syjsmk/recursivesample)
3. Install python module of jandy: <code>pip install http://localhost:3000/jandy-python.zip</code>
4. Set environment values for running profiling
  - TRAVIS_REPO_SLUG
  - TRAVIS_BUILD_ID
  - TRAVIS_BUILD_NUMBER
  - TRAVIS_BRANCH
5. Execute python script of jandy in local repository of target. It is included testing process by writting .jandy.yml
    
    ~/recursivesample# jandy
    
6. Waiting progress of uploading jandy's profiling results.
7. See <code>http://localhost:3000/repos/{account}/{repo}</code>
8. Enjoy!!


## Use optional parameters for execution jandy-server
Jandy-server uses spring-boot. Then, the configuration of spring-boot is used to configure jandy-server<br>
Example:

    ...
    multipart:
      maxFileSize: 512Mb
      maxRequestSize: -1
    ...
    
OR
    
    java -jar jandy-server/target/jandy-server-{version}.jar --multipart.maxFileSize=512Mb --multipart.maxRequestSize=-1
