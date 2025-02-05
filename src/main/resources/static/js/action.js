function changePageSize() {
    $("#itemSizeForm").submit();
}



function callDataMaster()
{
    var delimiter= '/';
    var sort = $('#sort').val();
    var idComp = $('#idComp').val();
    var descComp = $('#descComp').val();
    var path = $('#pathServer').val();
    var sortBy = $('#sortBy').val();
    var page = $('#currentPage').val();
    var column = $('#colName').val();
    var value = $('#textVal').val();
    var size = $('#sizeChange').val();
    var total = $('#totalData').val();
    page = ((page * size) > total)?0:page;
    var urlz =  delimiter.concat(path,'/',idComp,'/',descComp,'/',sort,'/',sortBy,'/',page,'?column=',column,'&value=',value,'&size=',size);
    $.get(urlz, function (data) {
        try{
            $('#data-result').html(data);
        }catch(r)
        {
            console.log('error '+r)
        }
    });
}

function callDataMasterPaging(page)
{
    var delimiter= '/';
    var sort = $('#sort').val();
    var idComp = $('#idComp').val();
    var descComp = $('#descComp').val();
    var path = $('#pathServer').val();
    var sortBy = $('#sortBy').val();
    var column = $('#colName').val();
    var value = $('#textVal').val();
    var size = $('#sizeChange').val();
    var urlz =  delimiter.concat(path,'/',idComp,'/',descComp,'/',sort,'/',sortBy,'/',page,'?column=',column,'&value=',value,'&size=',size);
    $.get(urlz, function (data) {
        try{
            $('#data-result').html(data);
        }catch(r)
        {
            console.log('error '+r)
            // $('#pilih-data').prop('disabled', false);
        }
    });
}

function callDataMasterSorting(sort,sortBy)
{
    var delimiter= '/';
    var idComp = $('#idComp').val();
    var descComp = $('#descComp').val();
    var path = $('#pathServer').val();
    var column = $('#colName').val();
    var page = $('#currentPage').val();
    var value = $('#textVal').val();
    var size = $('#sizeChange').val();
    var urlz =  delimiter.concat(path,'/',idComp,'/',descComp,'/',sort,'/',sortBy,'/',page,'?column=',column,'&value=',value,'&size=',size);
    $.get(urlz, function (data) {
        try{
            $('#data-result').html(data);
        }catch(r)
        {
            console.log('error '+r)
            // $('#pilih-data').prop('disabled', false);
        }
    });
}