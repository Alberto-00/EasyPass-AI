$(document).ready(function (){

    $.validator.addMethod("positiveNumber", function (value) {
        return /^[1-9]+[0-9]*$/.test(value)
    }, "Il numero inserito non è corretto.");

    $.validator.addMethod("checkNumberStudents", function (value) {
        const info = $('#roomSize').find(':selected').val();
        console.log(info.length)
        if (info.length > 0){
            const row = info.split("-")[0];
            const col = info.split("-")[1];
            return parseInt(value) <= parseInt(row) * parseInt(col) / 2;
        } return true;
    }, "Numero studenti troppo alto.");

    $("form[name='NumberOfStudentsForm']").validate({
        rules: {
            nStudents: {
                required: true,
                positiveNumber: true,
                checkNumberStudents: true,
            },
            roomSize: {
                required: true,
            }
        },
        messages: {
            nStudents: {
                required: "Inserire il numero di studenti.",
            },
            roomSize: {
                required: "Inserire l'aula.",
            }
        },
        submitHandler: function(form) {
            form.submit();
            $('.coll-2').css({'opacity' : 0.1, "z-index" : -1});
            $('#loading').css('display', 'flex');
        }
    });
})