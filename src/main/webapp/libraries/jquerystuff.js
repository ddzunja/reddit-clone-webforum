(function() {

    function readURL(input, imgTag) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $(imgTag).attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }

    $(document).on('change', '#imgThread', function() {
        readURL(this, '#img-preview-thread');
    });


    $(document).on('change', '#imgSubforum', function() {
        readURL(this, '#img-preview-subforum');
    });

    $(document).on('click', '#subforumId-field', function() {
        $('#subforumIdExists').hide();
    })

    $(document).on('click', '#threadId_field', function() {
        $('#threadIdExists').hide();
    })

})();