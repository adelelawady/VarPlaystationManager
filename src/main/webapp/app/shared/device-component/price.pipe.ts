import { Pipe, PipeTransform } from '@angular/core';
import { interval } from 'rxjs/internal/observable/interval';
import { map } from 'rxjs/operators';

@Pipe({
  name: 'devicePrice',
})
export class DevicePricePipe implements PipeTransform {
  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  transform(device: any, int: boolean) {
    return interval(10).pipe(
      map(() => {
        let diff = (new Date(device.session.start).getTime() - new Date().getTime()) / 1000;
        diff /= 60;
        const diffMin = Math.abs(Math.round(diff));
        // eslint-disable-next-line @typescript-eslint/restrict-plus-operands

        let pricePerHour = device.session.device.type.pricePerHour;
        if (device.session.multi) {
          pricePerHour = device.session.device.type.pricePerHourMulti;
        } else {
          pricePerHour = device.session.device.type.pricePerHour;
        }

        if (int) {
          return Math.round((diffMin / 60) * pricePerHour);
        } else {
          // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
          return Math.round((diffMin / 60) * pricePerHour) + ' LE';
        }
      })
    );
  }
}
