
/*
document.getElementsByTagName('select')[0].onchange = function() {
  var index = this.selectedIndex;
  var inputText = this.children[index].innerHTML.trim();
  console.log(inputText);
}
*/



function selectFunction(item) {


  var item_id = item.id;
  var item_value = item.value;
  var number_id = parseInt(item_id.toString().split("_")[2]);
  var total_hide = 4 - number_id;

  if (number_id !== 4){
    let text = "target_model_";
    if (item_value == -1) {
      for (var i = 1; i < total_hide+1; i++) {
        document.getElementById(text.concat(number_id + i).toString()).style.display = "none";
        document.getElementById(text.concat(number_id + i).toString()).value ="-1";

      }
    } else {
      document.getElementById(text.concat(number_id + 1).toString()).style.display = "block";
    }

  }
}
