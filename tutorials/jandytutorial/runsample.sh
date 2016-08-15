echo "input user ID : "
read user_id
# env | grep TRAVIS_REPO_SLUG

# git clone https://github.com/jcooky/recursivesample.git
# cd recursivesample
# gradle uploadArchives
git clone https://github.com/jcooky/jasypt-test.git
cd jasypt-test
mvn -DskipTests=true clean package
export TRAVIS_REPO_SLUG=$user_id/jasypt-test TRAVIS_BUILD_ID=1 TRAVIS_BRANCH=master TRAVIS_BUILD_NUMBER=1
python ../../../jandy-python/scripts/jandy
export TRAVIS_REPO_SLUG=$user_id/jasypt-test TRAVIS_BUILD_ID=2 TRAVIS_BRANCH=master TRAVIS_BUILD_NUMBER=2
python ../../../jandy-python/scripts/jandy
export TRAVIS_REPO_SLUG=$user_id/jasypt-test TRAVIS_BUILD_ID=3 TRAVIS_BRANCH=master TRAVIS_BUILD_NUMBER=3
python ../../../jandy-python/scripts/jandy

# GUI running? 

# unset TRAVIS_REPO_SLUG TRAVIS_BUILD_ID TRAVIS_BRANCH TRAVIS_BUILD_NUMBER
