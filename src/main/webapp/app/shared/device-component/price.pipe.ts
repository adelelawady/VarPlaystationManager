/* eslint-disable @typescript-eslint/no-unsafe-return */
import { Pipe, PipeTransform } from '@angular/core';
import { interval } from 'rxjs/internal/observable/interval';
import { map } from 'rxjs/operators';

@Pipe({
  name: 'devicePrice',
})
export class DevicePricePipe implements PipeTransform {
  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  transform(
    device: any,
    isNumber?: boolean,
    timeDiscount?: any,
    orderDiscount?: any,
    timeOnly?: boolean,
    ordersOnly?: boolean,
    addEGP?: boolean
  ) {
    if (!isNumber) {
      return interval(10).pipe(map(() => this.getDevicePrice(device, timeDiscount, orderDiscount, timeOnly, ordersOnly, addEGP)));
    }
    return this.getDevicePrice(device, timeDiscount, orderDiscount, timeOnly, ordersOnly, addEGP);
  }

  getDevicePrice(device: any, timeDiscount?: any, orderDiscount?: any, timeOnly?: boolean, ordersOnly?: boolean, addEGP?: boolean): any {
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
    let totalOrderprice = device.session.ordersPrice ? device.session.ordersPrice : 0;
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands

    if (orderDiscount && orderDiscount <= 100 && orderDiscount > 0 && totalOrderprice > 0) {
      totalOrderprice = Math.round(((100 - orderDiscount) * totalOrderprice) / 100);
    }
    let timePrice = Math.round((diffMin / 60) * pricePerHour);
    if (timeDiscount && timeDiscount <= 100 && timeDiscount > 0 && timePrice) {
      timePrice = Math.round(((100 - timeDiscount) * timePrice) / 100);
    }
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    let resultTotalPrice = timePrice + totalOrderprice;
    if (ordersOnly) {
      resultTotalPrice = totalOrderprice;
    }
    if (timeOnly) {
      resultTotalPrice = timePrice;
    }

    let resultFinal = 0;

    addEGP;

    if (resultTotalPrice > 0) {
      resultFinal = resultTotalPrice.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    } else {
      resultFinal = 0;
    }
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return addEGP ? 'EGP ' + resultFinal : resultFinal;
  }
}
