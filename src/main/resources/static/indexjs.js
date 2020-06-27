let playlistTokenArray = [];
let playlistObj;

function parseUrl() {
	refresh();
	
	let urlTxt = $("#playlistUrl").val();
	$("#playlistUrl").val("");
    let splitedStr = urlTxt.split("/");
	let token = splitedStr[splitedStr.length-1]
	
	$.ajax({
		async: false,
        url: "/parseurl",
        type: "POST",
        dataType: "text",
        data: token,
        success: function(data) {
        	playlistObj = JSON.parse(data);
            console.log(playlistObj);
            
            for (let i=0;i<playlistObj.length;i++) {
            	let track = playlistObj[i];
            	playlistTokenArray.push(track.videoId);
            }
            createPlayer();
            createPlaylist();
        },
        error: function() {

        }
    });
}

function refresh() {
	playlistTokenArray = [];	// 清空token array
	$("#playlistUl").html("");	// 清空播放清單
	$("#playerDiv").remove();	// 清空播放器
	$("#leftDiv").prepend('<div id="playerDiv"></div>');
}

function createPlaylist() {
	let target = $("#playlistUl");
	let track;
	
	for (let i=1;i<=playlistTokenArray.length;i++) {
		track = playlistObj[i-1];
		target.append(
			'<li>' +
				'<span id="list' + i + '" class="plSpan">' +
					i + '. ' + track.artist + ' - ' + track.title + '《' + track.album + '》' +
				'</span><br>' +
				'<a href="' + track.url + '">' + track.url + '</a>' +
			'</li>'
		)
	}
}

let playlistPlayer;
function createPlayer() {
	playlistPlayer = new YT.Player('playerDiv', {
	    height: '360',
	    width: '640',
	    videoId: playlistTokenArray[0],
	    events: {
	      'onReady': onPlayerReady,
	      'onStateChange': onPlayerStateChange
	    }
	});
}


// This code loads the IFrame Player API code asynchronously.
let tag = document.createElement('script');
tag.src = "https://www.youtube.com/iframe_api";
let firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// The API will call this function when the video player is ready.
function onPlayerReady(event) {
  event.target.playVideo();
  event.target.loadPlaylist(playlistTokenArray, 0, parseInt(0), 'default');
}

// The API calls this function when the player's state changes.
// The function indicates that when playing a video (state=1),
// the player should play for six seconds and then stop.
let done = false;
let playingIndex = 0;
function onPlayerStateChange(event) {
  /*if (event.data == YT.PlayerState.PLAYING && !done) {
    setTimeout(stopVideo, 6000);
    done = true;
  } */
  if (event.data == YT.PlayerState.ENDED && !done) {
	  setTimeout(playVideo, 3000);
  }
}

function stopVideo() {
  player.stopVideo();
}

function playVideo() {
	player.playVideo();
}
