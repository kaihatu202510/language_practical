$(function(){
    $('#submit').on('click',function(){
        submitButton()
    })
});

function submitButton(){
    let textForm1 = Number($('#text_form_1').val())
    let textForm2 = Number($('#text_form_2').val())
    let calcType = $('input[name="radio_calc_type"]:checked').val()
    let calcResult = 0
    switch(calcType) {
        case "plus":
            calcResult = textForm1 + textForm2
            break;
        case "minus":
            calcResult = textForm2 - textForm1
            break;
        default:
            alert('calc type was not selected.')
        return false;
    }
    alert('結果は'+ calcResult +'です。')
}