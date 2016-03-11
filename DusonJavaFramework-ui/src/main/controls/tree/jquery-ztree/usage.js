<ul id="treeDemo" class="ztree"></ul>


var zTreeObj;
   // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
   var setting = {};
   // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
   var zNodes = [
   {name:"test1", open:true, children:[
      {name:"test1_1"}, {name:"test1_2"}]},
   {name:"test2", open:true, children:[
      {name:"test2_1"}, {name:"test2_2"}]}
   ];
   $(document).ready(function(){
      zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
      var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
   });
   
	var setting = {
			async: {
				enable: true,
				url:"",
				autoParam:["id", "extra"]
			},
			view: {
				expandSpeed:"",
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false
			},
			edit: {
				enable: true,
				drag: {
					isCopy: false,
					isMove: false
				},
				showRemoveBtn: showRemoveBtn, // 或直接设置：true/false
				removeTitle: "remove",
				showRenameBtn: showRenameBtn,
				renameTitle: "rename"
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick,
				beforeRemove: beforeRemove,
				beforeRename: beforeRename,
				onRename: onRename,
				onRemove: onRemove
			}
		};
		
		var setting4Category = {
			async: {
				enable: true,
				url:"",
				autoParam:["id", "extra"]
			},
	        check: {
	            enable: true,
	            chkboxType: { "Y": "", "N": "" }
	        }
		};
		
		function showRemoveBtn(treeId, treeNode) {
			return !treeNode.isFirstNode;
		}
		function showRenameBtn(treeId, treeNode) {
			return !treeNode.isLastNode;
		}
		
		function beforeRemove(treeId, treeNode) {
			if(treeNode && treeNode.children && treeNode.children.length > 0){
				alert('error_delete_child_first');
				return false;
			}
			
			return confirm(treeNode.name + " confirm_delete");
		}
		function onRemove(e, treeId, treeNode) {
			// use treeNode.id to delete by ajax
		}
		
		function onClick(event, treeId, treeNode) { }
		function beforeRename(treeId, treeNode, newName) {
			if (newName.length == 0) {
				alert("error_no_empty");
				return false;
			}
			return true;
		}
		function onRename(event, treeId, treeNode, isCancel) {
			// use treeNode.id to update by ajax
		}

		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if ($("#addBtn_"+treeNode.id).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.id
				+ "' title='add node' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.id);
			if (btn) btn.bind("click", function(){
				var zTree = $.fn.zTree.getZTreeObj("treeCategory");
				
				// after add node by ajax success
				var parentNode = treeNode.id ? treeNode : null;
				zTree.addNodes(parentNode, {id:data.data, pId:treeNode.id, name:"", isParent:false});
				
				return false;
			});
		};
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.id).unbind().remove();
		};
