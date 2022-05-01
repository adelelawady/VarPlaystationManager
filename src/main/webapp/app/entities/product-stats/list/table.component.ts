import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { TableService } from '../service/table.service';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { combineLatest } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-table',
  templateUrl: './table.component.html',
})
export class TableComponent implements OnInit {
  languageDt = {
    loadingRecords: 'جارٍ التحميل...',
    lengthMenu: 'أظهر _MENU_ مدخلات',
    zeroRecords: 'لم يعثر على أية سجلات',
    info: 'إظهار _START_ إلى _END_ من أصل _TOTAL_ مدخل',
    search: 'ابحث:',
    paginate: {
      first: 'الأول',
      previous: 'السابق',
      next: 'التالي',
      last: 'الأخير',
    },
    aria: {
      sortAscending: ': تفعيل لترتيب العمود تصاعدياً',
      sortDescending: ': تفعيل لترتيب العمود تنازلياً',
    },
    select: {
      rows: {
        _: '%d قيمة محددة',
        '1': '1 قيمة محددة',
      },
      cells: {
        '1': '1 خلية محددة',
        _: '%d خلايا محددة',
      },
      columns: {
        '1': '1 عمود محدد',
        _: '%d أعمدة محددة',
      },
    },
    buttons: {
      print: 'طباعة',
      pageLength: {
        '-1': 'اظهار الكل',
        _: 'إظهار %d أسطر',
      },
      collection: 'مجموعة',
      copy: 'نسخ',
      copyTitle: 'نسخ إلى الحافظة',
      csv: 'CSV',
      excel: 'Excel',
      pdf: 'PDF',
      colvis: 'إظهار الأعمدة',
      colvisRestore: 'إستعادة العرض',
      copySuccess: {
        '1': 'تم نسخ سطر واحد الى الحافظة',
        _: 'تم نسخ %ds أسطر الى الحافظة',
      },
    },
    searchBuilder: {
      add: 'اضافة شرط',
      clearAll: 'ازالة الكل',
      condition: 'الشرط',
      data: 'المعلومة',
      logicAnd: 'و',
      logicOr: 'أو',
      title: ['منشئ البحث'],
      value: 'القيمة',
      conditions: {
        date: {
          after: 'بعد',
          before: 'قبل',
          between: 'بين',
          empty: 'فارغ',
          equals: 'تساوي',
          notBetween: 'ليست بين',
          notEmpty: 'ليست فارغة',
          not: 'ليست ',
        },
        number: {
          between: 'بين',
          empty: 'فارغة',
          equals: 'تساوي',
          gt: 'أكبر من',
          lt: 'أقل من',
          not: 'ليست',
          notBetween: 'ليست بين',
          notEmpty: 'ليست فارغة',
          gte: 'أكبر أو تساوي',
          lte: 'أقل أو تساوي',
        },
        string: {
          not: 'ليست',
          notEmpty: 'ليست فارغة',
          startsWith: ' تبدأ بـ ',
          contains: 'تحتوي',
          empty: 'فارغة',
          endsWith: 'تنتهي ب',
          equals: 'تساوي',
          notContains: 'لا تحتوي',
          notStarts: 'لا تبدأ بـ',
          notEnds: 'لا تنتهي بـ',
        },
        array: {
          equals: 'تساوي',
          empty: 'فارغة',
          contains: 'تحتوي',
          not: 'ليست',
          notEmpty: 'ليست فارغة',
          without: 'بدون',
        },
      },
      button: {
        '0': 'فلاتر البحث',
        _: 'فلاتر البحث (%d)',
      },
      deleteTitle: 'حذف فلاتر',
    },
    searchPanes: {
      clearMessage: 'ازالة الكل',
      collapse: {
        '0': 'بحث',
        _: 'بحث (%d)',
      },
      count: 'عدد',
      countFiltered: 'عدد المفلتر',
      loadMessage: 'جارِ التحميل ...',
      title: 'الفلاتر النشطة',
      showMessage: 'إظهار الجميع',
      collapseMessage: 'إخفاء الجميع',
    },
    infoThousands: ',',
    datetime: {
      previous: 'السابق',
      next: 'التالي',
      hours: 'الساعة',
      minutes: 'الدقيقة',
      seconds: 'الثانية',
      unknown: '-',
      amPm: ['صباحا', 'مساءا'],
      weekdays: ['الأحد', 'الإثنين', 'الثلاثاء', 'الأربعاء', 'الخميس', 'الجمعة', 'السبت'],
      months: ['يناير', 'فبراير', 'مارس', 'أبريل', 'مايو', 'يونيو', 'يوليو', 'أغسطس', 'سبتمبر', 'أكتوبر', 'نوفمبر', 'ديسمبر'],
    },
    editor: {
      close: 'إغلاق',
      create: {
        button: 'إضافة',
        title: 'إضافة جديدة',
        submit: 'إرسال',
      },
      edit: {
        button: 'تعديل',
        title: 'تعديل السجل',
        submit: 'تحديث',
      },
      remove: {
        button: 'حذف',
        title: 'حذف',
        submit: 'حذف',
        confirm: {
          _: 'هل أنت متأكد من رغبتك في حذف السجلات %d المحددة؟',
          '1': 'هل أنت متأكد من رغبتك في حذف السجل؟',
        },
      },
      error: {
        system: 'حدث خطأ ما',
      },
      multi: {
        title: 'قيم متعدية',
        restore: 'تراجع',
      },
    },
    processing: 'جارٍ المعالجة...',
    emptyTable: 'لا يوجد بيانات متاحة في الجدول',
    infoEmpty: 'يعرض 0 إلى 0 من أصل 0 مُدخل',
    thousands: '.',
    stateRestore: {
      creationModal: {
        columns: {
          search: 'إمكانية البحث للعمود',
          visible: 'إظهار العمود',
        },
        toggleLabel: 'تتضمن',
      },
    },
    autoFill: {
      cancel: 'إلغاء الامر',
      fillHorizontal: 'تعبئة الخلايا أفقيًا',
      fillVertical: 'تعبئة الخلايا عموديا',
    },
    decimal: ',',
    infoFiltered: '(مرشحة من مجموع _MAX_ مُدخل)',
  };
  dtOptions: any = {};
  tables?: any[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = 10000;
  page?: number;
  predicate!: string;
  ngbPaginationPage = 1;
  sortItem = 0;
  now: Date = new Date();
  from = dayjs().startOf('day'); // set to 12:00 am today
  to = dayjs().endOf('day'); // set to 23:59 pm today
  constructor(
    protected tableService: TableService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  fromDateChanged(date: any): void {
    // eslint-disable-next-line no-console
    this.from = dayjs(date.value);
    this.loadPage();
  }
  toDateChanged(date: any): void {
    // eslint-disable-next-line no-console
    this.to = dayjs(date.value);
    this.loadPage();
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    // eslint-disable-next-line no-console
    this.tableService
      .query(
        {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
        },
        this.sortItem,
        { from: this.from, to: this.to }
      )
      .subscribe(
        (res: HttpResponse<any[]>) => {
          this.isLoading = false;
          // eslint-disable-next-line no-console
          console.log(res);
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      lengthMenu: [10, 25, 50, 75, 100, 500, 1000],
      serverSide: false,
      processing: true,
      autoWidth: true,

      language: this.languageDt,
      order: [[1, 'desc']],
    };

    this.handleNavigation();
  }

  trackId(index: number, item: any): string {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return item.id!;
  }

  protected sort(): string[] {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    const result = [this.predicate];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);

      if (pageNumber !== this.page) {
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: any | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/admin/entities/product-stats'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
        },
      });
    }
    this.tables = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
