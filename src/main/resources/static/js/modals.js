function funcModalsHandler(event)
{
	var button = event.target
	var dataTitle = button.dataset.title
	var dataTarget = button.dataset.target
	var urlz = button.dataset.url
	var serverz = button.dataset.server
	$(dataTarget).on('show.bs.modal',function(){
		$.get(urlz, function (data) {
			let pattern = /Login/i;
			let result = data.match(pattern);
			try{
				if(result){
					window.location = "/auth/relogin";
				}else{
					$(serverz).html(data);
				}
			}catch(r)
			{
				console.log('error '+r)
			}finally
			{
				$(dataTarget).find('.modal-title').text(dataTitle)
			}
		});
	})

}

function getRequestHandler(event)
{
	var button = event.target
	var urlz = button.dataset.url
	var dataTitle = button.dataset.title
	$.get(urlz, function (data) {
		try{
			confirm('Data Berhasil Dihapus');
			window.location = "/"+dataTitle;
		}catch(r)
		{
			console.log('error '+r)
		}
	});
}