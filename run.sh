make
scp target/latc_x86_64.jar  mim:/home/students/inf/k/kl291649/OstatniaProsta/LatteCompiler/target
ssh -t mim 'cd  /home/students/inf/k/kl291649/OstatniaProsta/LatteCompiler && ./testGood.sh'
