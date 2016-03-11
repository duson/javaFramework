/* 时长
 $(element).jtimepicker({
	value: 1 // set init value
 });
 */
(function($) {
    $.fn.extend({
		
        jtimepicker: function(options) {

            var defaults = {
                orientation: 'horizontal',
                // set day
                dayCombo: 'daycombo',
                dayMode: 31,
                dayInterval: 1,
                dayDefaultValue: 0,
                dayLabel: '天',
                // set hours
                hourCombo: 'hourcombo',
                hourMode: 24,
                hourInterval: 1,
                hourDefaultValue: 0,
                hourLabel: '时',
                // set minutes
                minCombo: 'mincombo',
                minLength: 60,
                minInterval: 1,
                minDefaultValue: 0,
                minLabel: '分',
                // set seconds
                secView: true,
                secCombo: 'seccombo',
                secLength: 60,
                secInterval: 1,
                secDefaultValue: 0,
                secLabel: '秒'
            };

            var options = $.extend(defaults, options);
            
            return this.each(function() {
                var o = options;
                var $this = $(this);
                var html = '';
                var orientation = (o.orientation == 'horizontal') ? 'auto' : 'vertical';

                var val = $(this).val();
                if(o.value) val = o.value;

                if(val){
                	o.dayDefaultValue = parseInt(val / 86400);
                	val = val % 86400;
                	
                	o.hourDefaultValue = parseInt(val / 3600);
                	val = val % 3600;
                	
                	o.minDefaultValue = parseInt(val / 60);
                	o.secDefaultValue = val % 60;
                }
                
                var id = $this.attr("id");
                if(!id) id = "";
                html += "<span>";
                html += $this.createCombo(o.dayCombo+id, o.dayMode, o.dayInterval, o.dayDefaultValue, o.dayLabel);
                html += $this.createCombo(o.hourCombo+id, o.hourMode, o.hourInterval, o.hourDefaultValue, o.hourLabel);
                html += $this.createCombo(o.minCombo+id, o.minLength, o.minInterval, o.minDefaultValue, o.minLabel);
                html += $this.createCombo(o.secCombo+id, o.secLength, o.secInterval, o.secDefaultValue, o.secLabel);
                html += "</span>";

                var template = $(html);
                template.on('change', function(e) {
                    var day = parseInt($("#daycombo"+id, this).val());
                    var hour = parseInt($("#hourcombo"+id, this).val());
                    var min = parseInt($("#mincombo"+id, this).val());
                    var sec = parseInt($("#seccombo"+id, this).val());

                    $this.val(day * 86400 + hour * 3600 + min * 60 + sec).change();
                });

                $this.after(template);

                /*$(".jtimerpick-combo").change(function(){
                	var day = parseInt($("#daycombo"+id).val());
                	var hour = parseInt($("#hourcombo"+id).val());
                	var min = parseInt($("#mincombo"+id).val());
                	var sec = parseInt($("#seccombo"+id).val());
                	$this.val(day * 86400 + hour * 3600 + min * 60 + sec).change();
                });*/
                     
            }); 
        }     
    });

    $.fn.createCombo = function(id, length, interval, defValue, label) {
        var html = '<select style="width:60px;" class="jtimerpick-combo" id="' + id + '" name="' + id + '">';
        for(i = 0; i < length; i += interval) {
            var selected = i == defValue ? ' selected="selected"' : '';
            var txt = i < 10 ? '0' + i : i;
            html += '<option value="' + i + '"' + selected + '>' + txt + '</option>';
        }
        html += "</select>" + label;

        return html;
    }

})(jQuery);
