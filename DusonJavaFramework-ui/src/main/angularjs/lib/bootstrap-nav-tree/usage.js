
$scope.classify = [];
$scope.tree = {};	

function buildData() {
	var classify = [];
	
	var rootNodeObj = [{
		label : '全部',
		data: { id: 0, level: 0 },
		expanded : true
	}];
	var treeData = [];
	angular.forEach(classify, function(d) {
		if (d && d.parentId == 0) {
			treeData.push({
				label : d.classifyCname,
				data: { id: d.classifyId, level: 1 }
			});
		}
	});
	if (treeData.length > 0) {
		angular.forEach(treeData, function(d) {
			setChildNode(d, 2);
		});
	}
	rootNodeObj[0]['children'] = treeData;
	
	return rootNodeObj);
	
	function setChildNode(nodeObj, level) {
		var childNodeArr = [];
		angular.forEach(classify, function(d) {
			if (d.parentId == nodeObj.data.id) {
				childNodeArr.push({
					label : d.classifyCname,
					data: { id: d.classifyId, level: level }
				});
			}
		});

		nodeObj['children'] = childNodeArr;
		if (childNodeArr.length > 0) {
			angular.forEach(childNodeArr, function(d) {
				setChildNode(d, ++level);
			});
		}
	}
}

$scope.select= function(branch) {
	$scope.canEdit = branch.data.id !== 0;			
	$scope.canDelete = branch.data.id !== 0 && branch.children.length === 0;
	
	// load data				
	
	if(branch.data.level > 3)
		$scope.canAdd = false;
	else
		$scope.canAdd = true;
};
$scope.add = function() {
	 var selected = $scope.tree.get_selected_branch();
	 
	 $scope.curEditClassify = { 
		 belongType: $scope.currentBelongType,
		 parentId: selected ? selected.data.id : 0
	};
	
	 $('#editClassifyFormModal').modal({
		backdrop : 'static'
	 });
	 $('#editClassifyFormModal .modal-title').text('添加分类');
};
$scope.edit = function() {
	var selected = $scope.tree.get_selected_branch();
	
	$scope.curEditClassify = {
		id: selected.data.id,
		name: selected.label	
	};
	
	$('#editClassifyFormModal').modal({
		backdrop : 'static'
	});
	$('#editClassifyFormModal .modal-title').text('编辑分类');
};
$scope.save= function(curEditClassify) {
	var selected = $scope.tree.get_selected_branch();

	materialService.saveClassify(curEditClassify).then(function(data) {
		 if (data && data.success) {
			if(!curEditClassify.id) {
				if(!selected)
					selected = $scope.tree.get_first_branch();
				$scope.tree.add_branch(selected, {
			        label: curEditClassify.name,
			        data: {
			          id: data.object
			        }
				});
				
				if($scope.currentBelongType == 0)
					$scope.canDeletePersionalClassify = false;
				else
					$scope.canDeleteCompanyClassify = false;
			} else {
				selected.label = curEditClassify.name;
			}
			
			$('#editClassifyFormModal').modal('hide');
		 } else {
			 $('#editClassifyFormModal').modal('hide');
			 alertError(data.msg || '操作失败，请稍候重试');
		 }
	}, function(error) {
		alertError(error);
	});		
};
$scope.remove = function() {
	if(!confirm('确定此操作？')) return;
	
	var selected = $scope.tree.get_selected_branch();
	materialService.deleteClassify(selected.data.id).then(function(data) {
		if (data && data.success) {
			var parent = $scope.tree.get_parent_branch(selected);
			if (parent != null) {
				angular.forEach(parent.children, function(item, index) {
					if (angular.equals(item.data.id, selected.data.id)) {
						parent.children.splice(index, 1);
					}
				});
			}
			
			$scope.tree.select_branch(null);
		 } else {
			$('#editClassifyFormModal').modal('hide');
			 alertError(data.msg || data);
		 }
	});
};
