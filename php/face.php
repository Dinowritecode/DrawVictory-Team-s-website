<?php
$imgbase64 = $_POST['url']; //url就是前端图片的base64字符串
$info = explode(',',$imgbase64);
if($info[0] == 'data:image/png;base64'){
    upa($info[1]);

}else{
    echo 'no';
}
function upa($str)
{
    $path = './'; //保存位置
    $picname = '1.png';   //文件名及文件尾缀
    $base64_image = str_replace(null, '', $str);
    if (file_put_contents($path . $picname, base64_decode($base64_image))) {
        exit('success');
    } else {
        exit('fail');
    }
}
$img['result'] = $imgbase64;
echo json_encode($img);
?>