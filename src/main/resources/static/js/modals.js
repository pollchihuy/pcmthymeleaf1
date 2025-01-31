function funcModalsHandler(event)
{
	event.preventDefault(); //prevent default action
	var button = event.target
	var dataTitle = button.dataset.title
	var dataTarget = button.dataset.target
	var urlz = button.dataset.url
	var serverz = button.dataset.server
	console.log(dataTitle+' '+dataTarget+' '+urlz+' '+serverz);
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

// function funcModalsDataTableHandler(event)
// {
// 	var button = event.target
// 	var dataTitle = button.dataset.title
// 	var dataTarget = button.dataset.target
// 	var urlz = button.dataset.url
// 	var serverz = button.dataset.server
// 	$(dataTarget).on('show.bs.modal',function(){
//
// 		$.get(urlz, function (data) {
// 			try{
// 				$(serverz).html(data);
// 				$('#pilih-data').prop('disabled', true);
// 			}catch(r)
// 			{
// 				console.log('error '+r)
// 				$('#pilih-data').prop('disabled', false);
// 			}finally
// 			{
// 				$(dataTarget).find('#data').text(dataTitle)
// 			}
// 		});
// 	})
// }

function funcModalsDataTableHandler(dataTitle,dataTarget,urlz,serverz)
{
	console.log(dataTitle+'---'+dataTarget+'---'+urlz+'---'+serverz);
	// let myModal = new bootstrap.Modal(document.getElementById(dataTarget), {});
	$(dataTarget).on('show.bs.modal',function(){

		$.get(urlz, function (data) {
			try{
				$(serverz).html(data);
				console.log('Server Value -> '+serverz)
				// $('#pilih-data').prop('disabled', true);
			}catch(r)
			{
				console.log('error '+r)
				// $('#pilih-data').prop('disabled', false);
			}finally
			{
				$(dataTarget).find('#data-title').text(dataTitle)
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