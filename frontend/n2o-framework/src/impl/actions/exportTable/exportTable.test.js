import exportTable, { encodeQueryData } from './exportTable';
import sinon from 'sinon';

describe('exportTable', () => {
  it('encodeQueryData должен вернуть правильный query', () => {
    const data = {
      name: 'Sergey',
      surname: 'Liskov',
    };

    expect(encodeQueryData(data)).toBe('/export?name=Sergey&surname=Liskov');
  });

  it('exportTable должен вызывать destroyModal', () => {
    const dispatch = sinon.spy();

    exportTable({
      dispatch,
      widgetId: 'testWidget',
      state: {
        form: {
          exportTableForm: {
            values: {},
          },
        },
        widgets: {
          testWidget: {},
        },
      },
    });
    expect(dispatch.calledOnce).toBeTruthy();
  });
});
