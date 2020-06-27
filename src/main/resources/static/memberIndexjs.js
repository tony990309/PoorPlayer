function logOut() {
	$(location).attr('href', '/index');
}

$(document).ready(function () {
	getUserList();
});

function getUserList() {
	$.ajax({
		async: false,
		url: "/getuserplaylist",
		type: "GET",
		dataType: "text",
		success: function (data) {
			//console.log(data);
			let container = $("#accordion");
			let d = JSON.parse(data);
			console.log(d);
			let playlist;
			let tracklist;
      
			for (let i=1;i<=d.length;i++) {
				playlist = d[i-1];
				nameArray.push(playlist.name);
				container.append(
					'<div class="card">' +
    			   		'<div class="card-header" id="heading' + i + '">' +
    			   			'<h2 class="mb-0">' +
    			   				'<button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapse' + i + '" aria-expanded="true" aria-controls="collapse' + i + '">' +
    			   					playlist.name +
    			   				'</button>' +
    			   			'</h2>' +
    			   		'</div>' +
    			   		'<div id="collapse' + i + '" class="collapse" aria-labelledby="heading' + i + '" data-parent="#accordion">' +
    			   			'<div id="cb' + i + '" class="card-body">' +
    			   				'<input type="button" class="button" value="播放此清單" onclick="playThis(' + i + ')">' +
    			   				'<button type="button" class="btn btn-danger" onclick="deleteThis(' + i + ')">刪除此清單</button><br>' +
    			   			'</div>' +
    			   		'</div>' +
    			   	'</div>'
				);
    		
				tracklist = playlist.tracklist;
				userList.push(tracklist);
				let cb = "#cb"+i;
				let track;
				let tokenArr = [];
				
				for (let j=1;j<=tracklist.length;j++) {
					track = tracklist[j-1];
					tokenArr.push(track.videoId);
					
					$(cb).append(
						'<span>' +
							j + '. ' + track.artist + ' - ' + track.title + '《' + track.album + '》' +
						'</span><br>'
					)
				}
				
				userListTokenArray.push(tokenArr);
			}
		},
		error: function () {
		}
	})
}

let userListTokenArray = [];
let userList = [];
let nameArray = [];

function deleteThis(playlistIndex) {
	$.ajax({
		async: false,
        url: "/deleteplaylist?name=" + nameArray[playlistIndex-1],
        type: "DELETE",
        dataType: "text",
        headers: {
        	"Content-Type": "text;charset=utf-8"
        },
        success: function(data) {
        },
        error: function() {
        }
    });
	
	$("#accordion").html("");
	userListTokenArray = [];
	userList = [];
	getUserList();
}

function playThis(playlistIndex) {
	refresh();
	playlistTokenArray = userListTokenArray[playlistIndex-1];
	playlistObj = userList[playlistIndex-1];
	createPlayer();
    createPlaylist();
}

let playlistTokenArray = [];
let playlistObj;

function savePlaylist() {
	let str = JSON.stringify(playlistObj);
	let name = $("#listNameTxt").val();
	console.log(str);
	$.ajax({
		async: false,
        url: "/insertplaylist?name=" + name,
        type: "POST",
        dataType: "json",
        data: str,
        headers: {
        	"Content-Type": "application/json;charset=utf-8"
        },
        success: function(data) {
        },
        error: function() {
        }
    });
	
	$("#listNameTxt").val("");
	$("#saveBtn").attr("disabled", true);
	$("#accordion").html("");
	userList = [];
	getUserList();
}

function parseUrl() {
	refresh();
	$("#saveBtn").attr("disabled", false);	// 開放儲存播放清單
	
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
	playlistTokenArray = [];				// 清空token array
	$("#currentPlaylistUl").html("");		// 清空播放清單	
	$("#playerDiv").remove();				// 清空播放器
	$("#leftDiv").prepend('<div id="playerDiv"></div>');
}

function createPlaylist() {
	let target = $("#currentPlaylistUl");
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
	      'onReady': onPlayerReady
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

function stopVideo() {
  player.stopVideo();
}

function playVideo() {
	player.playVideo();
}
