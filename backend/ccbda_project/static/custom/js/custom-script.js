const total_numbers = 5;

const values_s = [];
const names_s = [];

function selectFunction(item) {

  var item_id = item.id;
  var item_value = item.value;
  var number_id = parseInt(item_id.toString().split("_")[2]);
  var total_hide = total_numbers - number_id;

  if (number_id !== 1){
    values_s.push(item.value);
    names_s.push(item.name);
  }

  console.log(values_s)

  if (number_id !== total_numbers){
    let text = "target_model_";
    if (item_value == -1) {
      for (var i = 1; i < total_hide+1; i++) {
        document.getElementById(text.concat(number_id + i).toString()).style.display = "none";
        document.getElementById(text.concat(number_id + i).toString()).value ="-1";
      }
    } else {
      if (number_id !== 1 && number_id !== 2){
        var next_select = document.getElementById(text.concat(number_id + 1).toString());

        if (number_id !== 4){
          //AJAX PETITION
        $.ajax({
          type: "POST",
          url: "/request",
          data: {value_championship: values_s[0], value_year: values_s[1]},
          success: function(data) {
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
        }else {
          //AJAX PETITION
        $.ajax({
          type: "POST",
          url: "/request",
          data: {value_championship: values_s[0], value_year: values_s[1], value_squad: values_s[2]},
          success: function(data) {

            const myArray = data.split("/");

            for(let i = 0; i<myArray.length; i++) {
              console.log(myArray[i]);
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

          document.getElementById(text.concat(number_id + 1).toString()).style.display = "block";
      }


    }
  }
}
