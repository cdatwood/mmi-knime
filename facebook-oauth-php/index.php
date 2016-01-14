<?php
date_default_timezone_set('America/New_York');
session_start();

require_once "lib/Facebook/autoload.php";

use Facebook\Facebook;
use Facebook\Helpers\FacebookRedirectLoginHelper;
use Facebook\Exceptions\FacebookSDKException;


$fb = new Facebook([
  //'app_id' => '959814390733501',
  //'app_secret' => '46ace08cce9c040eca0dbd080d83c252',
  'app_id' => '128490190584651',
  'app_secret' => 'ebcf28fe6505902c967f60ba40fbd7d0',
  'default_graph_version' => 'v2.5',
  //'default_access_token' => '{access-token}', // optional
]);

$helper = $fb->getRedirectLoginHelper();


try {
  $accessToken = $helper->getAccessToken();
  if (isset($accessToken)) {
    $response = $fb->get('/me?fields=id,name', $accessToken);
  }
} catch(Facebook\Exceptions\FacebookResponseException $e) {
  // When Graph returns an error
  echo 'Graph returned an error: ' . $e->getMessage();
  exit;
} catch(Facebook\Exceptions\FacebookSDKException $e) {
  // When validation fails or other local issues
  echo 'Facebook SDK returned an error: ' . $e->getMessage();
  exit;
}

if (isset($accessToken)) {

  $user = $response->getGraphUser();

  echo "Account ID: " . $user->getId() . "<br/>\n";
  echo "Access Token: " . $accessToken->getValue() . "<br/>\n";

  exit;
}

$permissions = [
'public_profile',
'user_friends',
'email',
'user_about_me',
'user_actions.books',
'user_actions.fitness',
'user_actions.music',
'user_actions.news',
'user_actions.video',
//'user_actions:fuuuuuuuuuuuuuuuu',
'user_birthday',
'user_education_history',
'user_events',
'user_games_activity',
'user_hometown',
'user_likes',
'user_location',
'user_managed_groups',
'user_photos',
'user_posts',
'user_relationships',
'user_relationship_details',
'user_religion_politics',
'user_tagged_places',
'user_videos',
'user_website',
'user_work_history',
'read_custom_friendlists',
'read_insights',
'read_audience_network_insights',
'read_page_mailboxes',
'manage_pages',
'publish_pages',
'publish_actions',
'rsvp_event',
'pages_show_list',
'pages_manage_cta',
'pages_manage_leads',
'ads_read',
'ads_management'
]; // optional
$loginUrl = $helper->getLoginUrl(isset($_SERVER['HTTPS'])?'https':'http'.'://'.$_SERVER['SERVER_NAME'].'/index.php', $permissions);

echo '<a href="' . $loginUrl . '">Log in with Facebook!</a>';

