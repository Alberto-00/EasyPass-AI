$(document).ready(function (){

    $.validator.addMethod("positiveNumber", function (value) {
        return /^[1-9]+[0-9]*$/.test(value)
    }, "Il numero inserito non è corretto.");

    $("form[name='NumberOfStudentsForm']").validate({
        rules: {
            nStudents: {
                required: true,
                positiveNumber: true,
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
        }
    });
})