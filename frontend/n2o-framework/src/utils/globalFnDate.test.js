import moment from 'moment';
import globalFnDate from './globalFnDate';
import evalExpression from './evalExpression';

const testFormat = { timeFormat: 'HH', dateFormat: 'DD.MM' };
const testFormatForArgs = { timeFormat: 'HH.mm.ss', dateFormat: 'DD.MM.YYYY' };

const stringFormats = `${testFormat.dateFormat} ${testFormat.timeFormat}`;
const stringFormatsToArgs = `${testFormatForArgs.dateFormat} ${
  testFormatForArgs.timeFormat
}`;

const date = globalFnDate.getFns();
globalFnDate.addFormat(testFormat);

describe('Проверка globalFnDate', () => {
  it('now', () => {
    expect(date.now()).toEqual(moment().format(stringFormats));
    expect(date.now(testFormatForArgs)).toEqual(
      moment().format(stringFormatsToArgs)
    );
  });
  it('nowUTC', () => {
    expect(date.nowUTC()).toEqual(moment.utc().format(stringFormats));
    expect(date.nowUTC(testFormatForArgs)).toEqual(
      moment.utc().format(stringFormatsToArgs)
    );
  });
  it('today', () => {
    expect(date.today()).toEqual(
      moment()
        .startOf('day')
        .format(stringFormats)
    );
    expect(date.today(testFormatForArgs)).toEqual(
      moment()
        .startOf('day')
        .format(stringFormatsToArgs)
    );
  });
  it('yesterday', () => {
    expect(date.yesterday()).toEqual(
      moment()
        .add(-1, 'd')
        .startOf('day')
        .format(stringFormats)
    );
    expect(date.yesterday(testFormatForArgs)).toEqual(
      moment()
        .add(-1, 'd')
        .startOf('day')
        .format(stringFormatsToArgs)
    );
  });
  it('tomorrow', () => {
    expect(date.tomorrow()).toEqual(
      moment()
        .add(1, 'd')
        .startOf('day')
        .format(stringFormats)
    );
    expect(date.tomorrow(testFormatForArgs)).toEqual(
      moment()
        .add(1, 'd')
        .startOf('day')
        .format(stringFormatsToArgs)
    );
  });
  it('beginDay', () => {
    expect(date.beginDay()).toEqual(
      moment()
        .startOf('day')
        .format(stringFormats)
    );
    expect(date.beginDay(testFormatForArgs)).toEqual(
      moment()
        .startOf('day')
        .format(stringFormatsToArgs)
    );
  });
  it('endDay', () => {
    expect(date.endDay()).toEqual(
      moment()
        .endOf('day')
        .format(stringFormats)
    );
    expect(date.endDay(testFormatForArgs)).toEqual(
      moment()
        .endOf('day')
        .format(stringFormatsToArgs)
    );
  });
  it('beginWeek', () => {
    expect(date.beginWeek()).toEqual(
      moment()
        .startOf('isoWeek')
        .format(stringFormats)
    );
    expect(date.beginWeek(testFormatForArgs)).toEqual(
      moment()
        .startOf('isoWeek')
        .format(stringFormatsToArgs)
    );
  });
  it('endWeek', () => {
    expect(date.endWeek()).toEqual(
      moment()
        .endOf('isoWeek')
        .format(stringFormats)
    );
    expect(date.endWeek(testFormatForArgs)).toEqual(
      moment()
        .endOf('isoWeek')
        .format(stringFormatsToArgs)
    );
  });
  it('beginMonth', () => {
    expect(date.beginMonth()).toEqual(
      moment()
        .startOf('month')
        .format(stringFormats)
    );
    expect(date.beginMonth(testFormatForArgs)).toEqual(
      moment()
        .startOf('month')
        .format(stringFormatsToArgs)
    );
  });
  it('endMonth', () => {
    expect(date.endMonth()).toEqual(
      moment()
        .endOf('month')
        .format(stringFormats)
    );
    expect(date.endMonth(testFormatForArgs)).toEqual(
      moment()
        .endOf('month')
        .format(stringFormatsToArgs)
    );
  });
  it('beginQuarter', () => {
    expect(date.beginQuarter()).toEqual(
      moment()
        .startOf('quarter')
        .format(stringFormats)
    );
    expect(date.beginQuarter(testFormatForArgs)).toEqual(
      moment()
        .startOf('quarter')
        .format(stringFormatsToArgs)
    );
  });
  it('endQuarter', () => {
    expect(date.endQuarter()).toEqual(
      moment()
        .endOf('quarter')
        .format(stringFormats)
    );
    expect(date.endQuarter(testFormatForArgs)).toEqual(
      moment()
        .endOf('quarter')
        .format(stringFormatsToArgs)
    );
  });
  it('beginYear', () => {
    expect(date.beginYear()).toEqual(
      moment()
        .startOf('year')
        .format(stringFormats)
    );
    expect(date.beginYear(testFormatForArgs)).toEqual(
      moment()
        .startOf('year')
        .format(stringFormatsToArgs)
    );
  });
  it('endYear', () => {
    expect(date.endYear()).toEqual(
      moment()
        .endOf('year')
        .format(stringFormats)
    );
    expect(date.endYear(testFormatForArgs)).toEqual(
      moment()
        .endOf('year')
        .format(stringFormatsToArgs)
    );
  });
});

describe('Проверка изменения глобального формата', () => {
  it('Проверка работы в globalFnDate', () => {
    globalFnDate.addFormat({ timeFormat: 'hh:ss', dateFormat: 'DD.MM' });
    expect(date.now()).toEqual(moment().format('DD.MM hh:ss'));
    globalFnDate.addFormat({
      timeFormat: 'hh:mm:ss',
      dateFormat: 'DD.MM.YYYY',
    });
    expect(date.now()).toEqual(moment().format('DD.MM.YYYY hh:mm:ss'));
  });
  it('Проверка работы evalExpression', () => {
    globalFnDate.addFormat({ timeFormat: 'HH:mm', dateFormat: 'DD.MM' });
    expect(evalExpression('$.now()', {})).toEqual(
      moment().format('DD.MM HH:mm')
    );
  });
});
