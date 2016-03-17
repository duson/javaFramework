<style>
	.webuploader-pick {background: none; padding: 0px;}
</style>

function initWebUploaderControl () {
	
	WebUploader.Uploader.register({
	    'before-send-file': 'preupload',
	    'after-send-file': 'afterupload'
	}, {
	    preupload: function(file) { // 计算文件Md5
	    	var owner = this.owner,
            deferred = WebUploader.Deferred();
	    	
	    	var uploadfile = getFileFromQueue(file.id);
			if(!uploadfile) return;
	    	
			// 通过Ajax判断是否登录超时
			
	    	owner.md5File(file)
	        .progress(function(percentage) {
	        	uploadfile.status = FileStatus.Calculation;
	            console.log('md5 Percentage:', percentage);
	        })
	        .then(function(md5) {
	            console.log('md5 result:', md5);
	            uploadfile.md5 = md5;
	            file.md5 = md5;
	            
	            // 判断秒传
				$.ajax('material/checkExist', {
					dataType: 'json',
					data: { md5: md5},
					success: function(response) {			
						if(response.success) {
							var exist = response.object;
							if(exist) {
								uploadfile.status = FileStatus.Exist;
								deferred.reject();
							}
							deferred.resolve(true);
						} else {
							uploadfile.status = FileStatus.Error;
							uploadfile.errorMsg = response.msg;
							
							deferred.reject();
						}
					},
					error: function(response) {
						uploadfile.status = FileStatus.Error;
						uploadfile.errorMsg = response.msg;
						
						deferred.reject();
					}
				});
	        });

	    	return deferred.promise();
	    },
	    
	    afterupload: function(file) { // 上传完入库
	    	var me = this,
            deferred = WebUploader.Deferred();
	    	var uploadfile = getFileFromQueue(file.id);
			if(!uploadfile) return;
			
			$.ajax('material/addMaterial', {
				method: 'Post',
				dataType: 'json',
				data: {fileName: file.name, fileSize: file.size, md5: file.md5},
				success: function(response) {
					if(response.success) {
						uploadfile.status = FileStatus.Complete;
						deferred.resolve();
					} else {
						uploadfile.status = FileStatus.Error;
						uploadfile.errorMsg = response.msg;
						
						deferred.reject();
					}
				}, 
				error: function(response) {
					uploadfile.status = FileStatus.Error;
					uploadfile.errorMsg = response.responseText;

					deferred.reject();
				}
			});
	
		    return deferred.promise();
		 }
	});
	
	uploader = WebUploader.create({
		swf: "Uploader.swf",
		server: "material/uploadFile",
		pick: {
            id: '.uploader', // 占位符
            multiple: true
		},
		auto: true,
		prepareNextFile: true,
		resize: false,
		threads: 1,
		//chunked: true,
		//runtimeOrder: 'flash,html5',
		fileSingleSizeLimit: 20 * 1024 * 1024, // 单文件上传大小限制
		accept: {
			title: '图片(*.jpg,*.jpeg,*.bmp,*.png) 视频(*.avi;*.mov;mpeg;*.mpg;*.mp4;*.rmvb;*.rm;*.dat;*.ts;*.wmv;*.flv)',
		    extensions: 'jpg,jpeg,bmp,png,avi,mov,mpeg,mpg,mp4,rmvb,rm,dat,ts,wmv,flv',
		    mimeTypes: 'image/jpeg,image/bmp,image/png,video/avi,video/quicktime,video/mpeg,video/mp4,video/application/vnd.rn-realmedia-vbr,application/vnd.rn-realmedia,application/octet-stream,video/mp2t,video/x-ms-wmv,video/x-flv'
		},
        formData: {}, // 传参
        duplicate: false,
        compress: false
	});
	
	uploader.on('fileQueued', function(file) {
		console.log('fileQueued');
		console.log(file);
		
		// 显示上传列表
		
		var uploadfile = getFileFromQueue(file.id);
		console.log(uploadfile);
		if(!uploadfile) {
			uploadfile = {
				id: file.id,
				name: file.name,
				fileSize: file.size,
				filePath: file.name
			};
			
			addUploadingQueue(uploadfile);
		}
		console.log(uploadfiles);
								
		uploadfile.status = FileStatus.Queued;
	});
	
	uploader.on('uploadBeforeSend', function(obj, data) {
		data.md5 = obj.file.md5;
		data.chunk = obj.chunk;
	});

	uploader.on('uploadProgress', function(file, percentage) {
		var uploadfile = getFileFromQueue(file.id);
		if(!uploadfile) return;
		uploadfile.percent = parseInt(percentage * 100);
		
		uploadfile.status = FileStatus.Progress;
		uploadfile.percent = parseInt(percentage * 100);
		
		uploadfile.statusText = uploadfile.percent + '%';
		
		console.log('uploadProgress');
		console.log(uploadfile);
	});

	uploader.on('uploadSuccess', function(file) {
		var uploadfile = getFileFromQueue(file.id);
		if(!uploadfile) return;
		uploadfile.status = 'complete';
		uploadfile.percent = 100;
		
		console.log('uploadSuccess');
		console.log(uploadfile);
	});
	
	uploader.on('uploadAccept', function(object, response) {
		console.log('uploadAccept');
		console.log(object);
		console.log(response);
		
		if(!response.success) {
			object.file.info = response.msg;
		} else if(response.object) {
			var fileName = response.object;
			if(fileName.indexOf('.') > 0)
				fileName = fileName.substring(0, fileName.indexOf('.'));
			object.file.md5 = fileName;
		}

		return response.success;
	});

	uploader.on('uploadError', function(file, reason) {
		console.log('uploadError');
		console.log(file);
		console.log(reason);
		
		var uploadfile = getFileFromQueue(file.id);
		if(!uploadfile) return;
		
		if(uploadfile.status !== FileStatus.Exist) {
			uploadfile.status = FileStatus.Error;
			uploadfile.errorMsg = file.info || uploadfile.errorMsg || '网络错误';
			uploadfile.statusText = file.errorMsg;
		}
	});
	
	uploader.on('error', function(type) {
		if(type === 'F_EXCEED_SIZE') {
			alert('上传文件不能超过' + WebUploader.formatSize(uploader.option('fileSingleSizeLimit')));
		} else if(type === 'Q_EXCEED_NUM_LIMIT') {
			alert('每次上传的文件个数不能超过' + uploader.option('fileNumLimit'));
		} else if(type === 'Q_EXCEED_SIZE_LIMIT') {
			alert('每次上传的总文件大小不能超过' + WebUploader.formatSize(uploader.option('fileSizeLimit')));
		} else if (type === "Q_TYPE_DENIED") {
			alert("请选择支持的文件类型");
		}
		console.log('error --' + type);
	});
	
	uploader.on('uploadComplete', function(file) {
		var uploadfile = getFileFromQueue(file.id);
		if(!uploadfile) return;
		
		if(uploadfile.status === FileStatus.Error)
			uploadfile.statusText = uploadfile.errorMsg;
		else
			uploadfile.statusText = FileStatus.getStatusText(uploadfile.status);
		
		console.log('uploadComplete');
		console.log(uploadfile);
	});
	
	uploader.on('uploadFinished', function() {
		console.log('上传完毕');
	});

}

function webUploadCancel(uploadfile) {
	var status = uploadfile.status;
	
	if(status === FileStatus.Progress) // 如果正在传输先暂停
		uploader.stop(uploadfile);
	
	uploader.cancelFile(uploadfile.id); // 标记文件状态为已取消, 同时将中断文件传输
	uploader.removeFile(uploadfile.id, true); // 标记文件状态为已取消， 从 queue 中移除

    uploadfile.status = FileStatus.Cancelled;
    
    if(uploader.getStats().queueNum > 0)
    	uploader.upload();
}
function webUplloadCancelAll() {
	var files = uploader.getFiles();
	$.each(files, function(file, idx) {
		uploader.removeFile(file);
	});
	
	uploadfiles = [];
}

function addUploadingQueue(file) {
	if(file.filePath && !file.name) {
    	var name = file.filePath.replace('\\', '/');
    	file.name = name.substring(name.lastIndexOf('/')+1);
	}
	
	uploadfiles.push(file);
}

function getFileFromQueue(id) {
	// 根据id从uploadfiles中获取文件
}
