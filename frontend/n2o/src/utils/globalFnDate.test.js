import moment from 'moment';
import globalFnDate from './globalFnDate';

const testFormat = { timeFormat: 'hh', dateFormat: 'DD.MM' };
const stringFormats = `${testFormat.dateFormat} ${testFormat.timeFormat}`;

const date = globalFnDate.getFns();
globalFnDate.addFormat(testFormat);

describe('Проверка globalFnDate', () => {
  it('now', () => {
    expect(date.now()).toEqual(moment().format(stringFormats));
  });
  it('nowUTC', () => {
    expect(date.nowUTC()).toEqual(moment.utc().format(stringFormats));
  });
  it('yesterday', () => {
    expect(date.yesterday()).toEqual(
      moment()
        .add(-1, 'd')
        .format(stringFormats)
    );
  });
  it('tomorrow', () => {
    expect(date.tomorrow()).toEqual(
      moment()
        .add(1, 'd')
        .format(stringFormats)
    );
  });
  it('beginDay', () => {
    expect(date.beginDay()).toEqual(
      moment()
        .startOf('day')
        .format(stringFormats)
    );
  });
  it('endDay', () => {
    expect(date.endDay()).toEqual(
      moment()
        .endOf('day')
        .format(stringFormats)
    );
  });
});
