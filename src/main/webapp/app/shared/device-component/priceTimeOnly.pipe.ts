/* eslint-disable @typescript-eslint/no-unsafe-return */
import { Pipe, PipeTransform } from '@angular/core';
import { interval } from 'rxjs/internal/observable/interval';
import { map } from 'rxjs/operators';

@Pipe({
  name: 'devicePriceTimeOnly',
})
export class DevicePricePipeTimeOnly implements PipeTransform {
  formatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'EGP',

    // These options are needed to round to whole numbers if that's what you want.
    //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
    //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
  });
  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  transform(device: any) {
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

        // eslint-disable-next-line @typescript-eslint/no-unsafe-return
        // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
        return this.formatter.format(Math.round((diffMin / 60) * pricePerHour));
      })
    );
  }
}
