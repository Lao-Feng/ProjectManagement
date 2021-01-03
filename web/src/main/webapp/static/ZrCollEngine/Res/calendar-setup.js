var oldLink = null;
function setActiveStyleSheet(link, title) {
  var i, a, main;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
      a.disabled = true;
      if(a.getAttribute("title") == title) a.disabled = false;
    }
  }
  if (oldLink) oldLink.style.fontWeight = 'normal';
  oldLink = link;
  link.style.fontWeight = 'bold';
  return false;
}

function selected(cal, date) {
  cal.sel.value = date;
  DoChange(cal.sel.name);    //add by ZrCollEngine//if (cal.dateClicked && (cal.sel.id == "sel1" || cal.sel.id == "sel3"))//ÐèÒªË«»÷
  if (cal.dateClicked)
  {cal.callCloseHandler();}
}

function closeHandler(cal) {
  cal.hide();                        // hide the calendar
}

var addx = 0;
var addy = 0;
function showCalendar(id, format) {

  var el = document.getElementById(id);

  if (calendar != null) {
    calendar.hide();                 // so we hide it first.
  } else {
    var cal = new Calendar(false, null, selected, closeHandler);
    calendar = cal;                  // remember it in the global var
    cal.setRange(1900, 2070);        // min/max year allowed.
    cal.create();
  }
  calendar.setDateFormat(format);    // set the specified date format
  calendar.parseDate(el.value);      // try to parse the text in field
  calendar.sel = el;                 // inform it what input field we use
  calendar.showAtElement(el.nextSibling, "Br");        // show the calendar

  return false;
}

var MINUTE = 60 * 1000;
var HOUR = 60 * MINUTE;
var DAY = 24 * HOUR;
var WEEK = 7 * DAY;

function isDisabled(date) {
  var today = new Date();
  return (Math.abs(date.getTime() - today.getTime()) / DAY) > 10;
}

function flatSelected(cal, date) {
  var el = document.getElementById("preview");
  el.innerHTML = date;
}

function showFlatCalendar() {
  var parent = document.getElementById("display");
  var cal = new Calendar(false, null, flatSelected);
  cal.weekNumbers = false;
  cal.setDisabledHandler(isDisabled);
  cal.setDateFormat("DD, M d");
  cal.create(parent);
  cal.show();
}
