$(document).ready(function () {
    $("#q").autocomplete({
        serviceUrl: '/api/v1/products/unused/search',
        showNoSuggestionNotice: true,
        noSuggestionNotice: "<div class=\"autocomplete-suggestion\" data-index=\"1\">No product found.</div>",
        lookupLimit: 3,
        minChars: 3,
        onSelect: function (suggestion) {
            $("#addProductKey").val(suggestion.data);
            $("#addProduct").submit();
        }
    });

    $(".remove-product").click(function () {
        $("#deleteProductKey").val($(this).data('id'));
        $("#deleteProduct").submit();
    });

    var timer;
    $("#editEvent .form-control").not(".no-trigger").keyup(function () {
        startTimer($("#editEvent"));
    }).keydown(function () {
        clearTimeout(timer);
    });

    $("#editEventOptions .form-control").change(function () {
        startTimer($("#editEventOptions"));
    });

    $("#productDatatable").DataTable({
        searching: false,
        paging: false,
        info: false,
        columnDefs: [
            {
                width: "81px",
                targets: 3,
                orderable: false
            }
        ]
    });

    function startTimer(form) {
        clearTimeout(timer);
        $(".spinner").addClass('active');
        timer = setTimeout(function () {
            if (form[0].checkValidity()) {
                form.submit();
            }
            $(".spinner").removeClass('active');
        }, 1000);
    }
});
