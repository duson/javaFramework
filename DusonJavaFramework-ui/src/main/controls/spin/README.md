# [jQuery Spinner](https://vsn4ik.github.io/jquery.spinner/)

[![Bower version](https://img.shields.io/bower/v/jquery.spinner.svg)](https://github.com/vsn4ik/jquery.spinner)
[![License](https://img.shields.io/npm/l/jquery.spinner.svg)][license]
[![Build Status](https://travis-ci.org/vsn4ik/jquery.spinner.svg)](https://travis-ci.org/vsn4ik/jquery.spinner)
[![devDependency Status](https://david-dm.org/vsn4ik/jquery.spinner/dev-status.svg)](https://david-dm.org/vsn4ik/jquery.spinner#info=devDependencies)


A Number-Spinner based-on jQuery, Support Keyboard operations and continuous changing.

## Basic usage, it's very simple
```html
<!-- // ref javascript file -->
<script src="dist/jquery.spinner.min.js"></script>

<!-- // spinner plugin DOM -->
<div data-trigger="spinner">
  <a href="javascript:;" data-spin="down">-</a>
  <input type="text" value="1" data-rule="quantity">
  <a href="javascript:;" data-spin="up">+</a>
</div>
```

## Getting Started
Download the [production version][min] or the [development version][max].

In your web page:

```html
<script src="jquery.js"></script>
<script src="dist/jquery.spinner.min.js"></script>
<script>
$("#spinner")
  .spinner('delay', 200) //delay in ms
  .spinner('changed', function(e, newVal, oldVal) {
    //trigger lazed, depend on delay option.
  })
  .spinner('changing', function(e, newVal, oldVal) {
    //trigger immediately
  });
</script>

<div data-trigger="spinner" id="spinner">
  <a href="javascript:;" data-spin="down">-</a>
  <input type="text" value="1" data-rule="quantity">
  <a href="javascript:;" data-spin="up">+</a>
</div>
```

## Documentation
### Spinner options

#### delay
> delay to fire changed event in millisecond, default is 500.

#### changed
> changed event handler, the changed event is a lazy-mode event, default is null.

#### changing
> changing event handler, the changing event will be fired immediately, default is null.

### Spinning Options(setup via data-api)
#### min
> the minimum value, default is null.

#### max
> the maximum value, default is null.

#### step
> the changing-value of per-step, if passed as a function, the function will be called within the spinner object scope.

#### precision
> the precision of value

### Built-in rules
```javascript
  currency: { min: 0.00, max: null, step: 0.01, precision: 2 },
  quantity: { min: 1, max: 999, step: 1, precision:0 },
  percent:  { min: 1, max: 100, step: 1, precision:0 },
  month:    { min: 1, max: 12, step: 1, precision:0 },
  day:      { min: 1, max: 31, step: 1, precision:0 },
  hour:     { min: 0, max: 23, step: 1, precision:0 },
  minute:   { min: 1, max: 59, step: 1, precision:0 },
  second:   { min: 1, max: 59, step: 1, precision:0 }
```
Usage:
```html
<input type="text" value="1" data-rule="quantity">
```

## Examples

### Work with Bootstrap 3 and Font Awesome 4

```html
<link href="dist/bootstrap-spinner.css" rel="stylesheet">

<div class="input-group spinner" data-trigger="spinner">
  <input type="text" class="form-control text-center" value="1" data-rule="quantity">
  <span class="input-group-addon">
    <a href="javascript:;" class="spin-up" data-spin="up"><i class="fa fa-caret-up"></i></a>
    <a href="javascript:;" class="spin-down" data-spin="down"><i class="fa fa-caret-down"></i></a>
  </span>
</div>
```

### Customize

#### specify a field

```html
<div data-trigger="spinner">
  <input type="text" value="0" title="this field isn't a spinning.">
  <input type="text" value="1" data-spin="spinner" data-rule="quantity" data-max="10">
</div>
```

#### Use hidden field

```html
<div data-trigger="spinner" id="spinner">
  <span id="spinner-value"></span>
  <input type="hidden" value="1" data-spin="spinner" data-rule="quantity" data-max="10">
  <a href="javascript:;" data-spin="down">-</a>
  <a href="javascript:;" data-spin="up">+</a>
</div>

<script>
  $("#spinner").spinner('changing', function(e, newVal, oldVal) {
    $('#spinner-value').html(newVal);
  });
</script>
```

#### pass step options as a function
```javascript
//To skip 0
$("#spinner").spinner({
  step: function(dir) {
    //'this' references to the spinner object
    if ((this.oldValue === 1 && dir === 'down') || (this.oldValue === -1 && dir === 'up')) {
      return 2;
    }
    return 1;
  }
});

//or use API syntax
$("#spinner").spinner('step', function(dir) {
  //your logic here
});
```


## Copyright and license

Copyright xixilive, 2013&ndash;2016.

Licensed under [the MIT License][license].

[license]: https://github.com/vsn4ik/jquery.spinner/blob/master/LICENSE
[min]: https://raw.githubusercontent.com/vsn4ik/jquery.spinner/master/dist/js/jquery.spinner.min.js
[max]: https://raw.githubusercontent.com/vsn4ik/jquery.spinner/master/dist/js/jquery.spinner.js
