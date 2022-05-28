const total_numbers = 5;

function selectFunction(item) {

  var item_id = item.id;
  var item_value = item.value;
  var number_id = parseInt(item_id.toString().split("_")[2]);
  var total_hide = total_numbers - number_id;

  if (number_id !== total_numbers){
    let text = "target_model_";
    if (item_value == -1) {
      /*
        var selector_text_1 = '#target_model_'+(4).toString()+' option:not(:first)';
        $(selector_text_1).remove();
        var selector_text_2 = '#target_model_'+(5).toString()+' option:not(:first)';
        $(selector_text_2).remove();
       */

      for (let i = 1; i < total_hide+1; i++) {
        document.getElementById(text.concat(number_id + i).toString()).style.display = "none";

        document.getElementById(text.concat(number_id + i).toString()).value ="-1";
      }
    } else {
      if (number_id !== 1 && number_id !== 2){
        var next_select = document.getElementById(text.concat(number_id + 1).toString());

        if (number_id === 3){
          let value_c = $('#target_model_2').val();
          let value_y = $('#target_model_3').val();

          //AJAX PETITION
          $.ajax({
            type: "POST",
            url: "/request",
            data: {value_championship: value_c, value_year: value_y},
            success: function(data) {
              $('#target_model_4 option:not(:first)').remove();
              $('#target_model5 option:not(:first)').remove();
              console.log(data);
              const myArray = data.split("/");
              for(let i = 0; i<myArray.length; i++) {
                var option = document.createElement("option");
                option.value = myArray[i];
                option.text = myArray[i];
                next_select.appendChild(option);
              }
                next_select.style.display = "block";
                }
          });
        }else if (number_id === 4) {
          let value_c = $('#target_model_2').val();
          let value_y = $('#target_model_3').val();
          let value_s = $('#target_model_4').val();

          //AJAX PETITION
        $.ajax({
          type: "POST",
          url: "/request",
          data: {value_championship: value_c, value_year: value_y, value_squad: value_s},
          success: function(data) {
            $('#target_model_'+(5).toString()+' option:not(:first)').remove();
            const myArray = data.split("/");
            for(let i = 0; i<myArray.length; i++) {

              var option = document.createElement("option");
              option.value = myArray[i];
              option.text = myArray[i];

              next_select.appendChild(option);
            }

              next_select.style.display = "block";
              }
        });
        }

      }else {
        if (number_id === 2){
           $('#target_model_'+(3).toString()).val('-1');

        }

          document.getElementById(text.concat(number_id + 1).toString()).style.display = "block";
      }


    }
  }
}
