import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'replaceLineBreaks'})
export class ReplaceLineBreaksPipe implements PipeTransform {
  transform(value: string, custom?: string): string {
    return custom ? value.split(custom).join('<br/>') : value.split(/\n/g).join('<br/>');
  }
}
