import { Pipe, PipeTransform } from '@angular/core';
import { interval } from 'rxjs/internal/observable/interval';
import { map } from 'rxjs/operators';

@Pipe({
  name: 'countdowntimer',
})
export class CountdownPipe implements PipeTransform {
  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  transform(time: string) {
    return interval(10).pipe(
      map(() => {
        let diff = (new Date(time).getTime() - new Date().getTime()) / 1000;
        diff /= 60;
        const diffMin = Math.abs(Math.round(diff));
        return this.timeConvert(diffMin);
      })
    );
  }

  timeConvert(n: number): string {
    const num = n;
    const hours = num / 60;
    const rhours = Math.floor(hours);
    const minutes = (hours - rhours) * 60;
    const rminutes = Math.round(minutes);
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    if (rhours === 0) {
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      return rminutes + ' m';
    } else {
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      return rhours + ' h : ' + rminutes + ' m';
    }
  }
}
