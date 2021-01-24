<?php
error_reporting(0);

    $name = $_POST["name"]; 
    $image = $_POST["image"];
    $response = array();

    $decodedImage = base64_decode("$image");
    $return = file_put_contents("imgess/".$name.".JPG", $decodedImage);
 
    if($return !== 0){

          $response['success'] = "1";
          $response['message'] = "Image Uploaded Successfully with Retrofit";
    
          }else{
          
          $response['success']="0";
          $response['message'] = "Image Upload Failed";

        }
 
    echo json_encode($response);
?>
