import {
  widgetsSelector,
  makeWidgetByIdSelector,
  makeWidgetIsInitSelector,
  makeWidgetVisibleSelector,
  makeWidgetEnabledSelector,
  makeWidgetLoadingSelector,
  makeWidgetSizeSelector,
  makeWidgetCountSelector,
  makeWidgetSortingSelector,
  makeWidgetFilterVisibilitySelector,
  makeWidgetValidationSelector,
  makeSelectedIdSelector,
  makeIsActiveSelector,
  makeWidgetPageSelector,
  makeWidgetPageIdSelector,
  makeTypeSelector,
  makeWidgetDataProviderSelector,
  isAnyTableFocusedSelector
} from './widgets';

const state = {
  widgets: {
    widget1: {
      isInit: true,
      isVisible: true,
      isEnabled: true,
      isLoading: true,
      size: 10,
      count: 100,
      page: 1,
      pageId: '_',
      sorting: {
        name: 'DESC'
      },
      isFilterVisible: true,
      validation: {
        some: 'value'
      },
      selectedId: 2,
      isActive: true,
      type: 'table',
      dataProvider: {
        url: 'test',
        pathMapping: {},
        queryMapping: {}
      }
    },
    widget2: {}
  }
};

describe('Проверка селекторов widgets', () => {
  it('widgetsSelector должен вернуть widgets', () => {
    expect(widgetsSelector(state)).toEqual(state.widgets);
  });
  it('widgetsSelector должен вернуть widget по id', () => {
    expect(makeWidgetByIdSelector('widget1')(state)).toEqual(state.widgets.widget1);
  });
  it('makeWidgetIsInitSelector должен вернуть isInit по id', () => {
    expect(makeWidgetIsInitSelector('widget1')(state)).toEqual(state.widgets.widget1.isInit);
  });
  it('makeWidgetVisibleSelector должен вернуть visible по id', () => {
    expect(makeWidgetVisibleSelector('widget1')(state)).toEqual(state.widgets.widget1.isVisible);
  });
  it('makeWidgetEnabledSelector должен вернуть enabled по id', () => {
    expect(makeWidgetEnabledSelector('widget1')(state)).toEqual(state.widgets.widget1.isEnabled);
  });
  it('makeWidgetLoadingSelector должен вернуть loading по id', () => {
    expect(makeWidgetLoadingSelector('widget1')(state)).toEqual(state.widgets.widget1.isLoading);
  });
  it('makeWidgetSizeSelector должен вернуть size по id', () => {
    expect(makeWidgetSizeSelector('widget1')(state)).toEqual(state.widgets.widget1.size);
  });
  it('makeWidgetCountSelector должен вернуть count по id', () => {
    expect(makeWidgetCountSelector('widget1')(state)).toEqual(state.widgets.widget1.count);
  });
  it('makeWidgetSortingSelector должен вернуть count по id', () => {
    expect(makeWidgetSortingSelector('widget1')(state)).toEqual(state.widgets.widget1.sorting);
  });
  it('makeWidgetFilterVisibilitySelector должен вернуть isFilterVisible по id', () => {
    expect(makeWidgetFilterVisibilitySelector('widget1')(state)).toEqual(
      state.widgets.widget1.isFilterVisible
    );
  });
  it('makeWidgetValidationSelector должен вернуть validation по id', () => {
    expect(makeWidgetValidationSelector('widget1')(state)).toEqual(
      state.widgets.widget1.validation
    );
  });
  it('makeSelectedIdSelector должен вернуть selectedId по id', () => {
    expect(makeSelectedIdSelector('widget1')(state)).toEqual(state.widgets.widget1.selectedId);
  });
  it('makeIsActiveSelector должен вернуть isActive по id', () => {
    expect(makeIsActiveSelector('widget1')(state)).toEqual(state.widgets.widget1.isActive);
  });
  it('makeWidgetPageSelector должен вернуть page по id', () => {
    expect(makeWidgetPageSelector('widget1')(state)).toEqual(state.widgets.widget1.page);
  });
  it('makeWidgetPageIdSelector должен вернуть pageId по id', () => {
    expect(makeWidgetPageIdSelector('widget1')(state)).toEqual(state.widgets.widget1.pageId);
  });
  it('makeTypeSelector должен вернуть type по id', () => {
    expect(makeTypeSelector('widget1')(state)).toEqual(state.widgets.widget1.type);
  });
  it('makeWidgetDataProviderSelector должен вернуть type по id', () => {
    expect(makeWidgetDataProviderSelector('widget1')(state)).toEqual(
      state.widgets.widget1.dataProvider
    );
  });
  it('isAnyTableFocusedSelector должен вернуть true', () => {
    expect(isAnyTableFocusedSelector(state)).toEqual(true);
  });
});
