import CommonAjax from './helper';
import layer from '../../libs/plugins/layer/layer';
import Form from './form';

var user = {
	dlgAddUserTag : function() {
		layer.open({
			type : 1,
			title : '添加标签',
			skin : 'layui-layer-molv',
			shadeClose : true,
			resize : false,
			scrollbar : false,
			area : [ '400px', 'auto' ],
			btn : [ '确定' ],
			content : '<div class="p-10">' +
				'<div class="form-group">' +
				'<label for="tag-title">标签名称</label>' +
				'<input type="text" class="form-control" id="tag-title" placeholder="请输入标签名称">' +
				'</div>' +
				'<div class="form-group">' +
				'<label for="tag-description">标签说明</label>' +
				'<textarea class="form-control" id="tag-description" placeholder="请输入标签说明"></textarea>' +
				'</div>' +
				'</div>',
			yes : function(index, layero) {

				var tag_title = $('#tag-title').val();
				var tag_description = $('#tag-description').val();
				if (tag_title.length < 1) {
					layer.msg('请输入标签名称！', {
						icon : 2,
						time : 1000
					});
					return false;
				}

				var model = {
					title : tag_title,
					description : tag_description
				};
				var loading = layer.load();
				CommonAjax.postJSON('/user-ajax/add-tag', JSON.stringify(model),
					function(res) {
						layer.close(loading);
						Form.defaultCallback(res, {
							success : function() {
								layer.close(index);
								layer.msg('添加成功！', {
									icon : 1,
									time : 1000
								}, function() {
									location.reload();
								});
							},
							error : function() {
								layer.msg('添加失败！', {
									icon : 2,
									time : 1000
								});
							}
						});

					});
			}
		});
	},
	queryUserTagsPagely : function(page, pageSize) {
		var loading = layer.load();
		var model = {
			page : page,
			pageSize : pageSize
		};
		CommonAjax.postJSON('/user-ajax/query-user-tags-pagely', JSON.stringify(model),
			function(res) {
				layer.close(loading);
				Form.defaultCallback(res, {
					success : function() {
						var html = '<table class="table table-condensed table-hover">' +
							'<thead>' +
							'<tr>' +
							'<td><label>名称</label></td>' +
							'<td><label>说明</label></td>' +
							'<td></td>' +
							'</tr>' +
							'</thead><tbody>';
						$.each(res.data, function(i, o) {
							html += '<tr data-id="' + o.id + '">' +
								'<td>' + o.title + '</td>' +
								'<td>' + o.description + '</td>' +
								'<td><a href="javascript:;" class="del">删除</a> <a href="javascript:;" class="edit">编辑</a></td>' +
								'</tr>';
						});
						html += '</tbody></table>';

						layer.open({
							type : 1,
							skin : 'layui-layer-molv',
							shadeClose : true,
							resize : false,
							area : [ '400px', 'auto' ],
							title : '标签管理',
							content : html,
							success : function(layero, index) {
								$(layero).find('.del').on('click', function() {
									var me = this;
									var _id = $(me).parents('tr').first().attr('data-id');
									layer.open({
										type : 0,
										skin : 'layui-layer-molv',
										icon : 2,
										shadeClose : true,
										resize : false,
										content : '确定删除？',
										yes : function(index1, layero) {
											var loading1 = layer.load();
											CommonAjax.postJSON('/user-ajax/del-tag-by-id/' + _id, '',
												function(v1) {
													layer.close(loading1);
													layer.close(index1);
													if (v1 == 1) {
														layer.msg('删除成功！', {
															icon : 1,
															time : 1000
														});
														$(me).parents('tr').first().slideUp('slow');
													} else {
														layer.msg('删除失败！', {
															icon : 2,
															time : 1000
														});
													}
												});
										}
									});
								});
							},

							yes : function(index, layero) {
								layer.close(index);
							}
						});
					},
					error : function() {
						layer.msg('加载失败！', {
							icon : 2,
							time : 1000
						});
					}
				});


			});
	}
};

export default user;