import {
  pagesSelector,
  makePageByIdSelector,
  makePageMetadataByIdSelector,
  makePageLoadingByIdSelector,
  makePageRoutesByIdSelector,
  makeWidgetMetadataSelector,
  makePageActionsByIdSelector,
  makePageToolbarByIdSelector,
  makePageErrorByIdSelector,
  makePageTitleByIdSelector,
  makePageDisabledByIdSelector,
} from './pages';

const state = {
  pages: {
    _: {
      metadata: {
        widgets: {
          widgetId: {
            metadata: 'widget metadata',
          },
        },
        page: {
          title: 'page title',
          some: 'value',
        },
      },
      loading: true,
      error: false,
      disabled: true,
      routes: [
        {
          route: 'route',
        },
        {
          route: 'route',
        },
      ],
      actions: [
        {
          action: 'action',
        },
      ],
      toolbar: 'toolbar object',
    },
  },
};

describe('Проверка селекторов pages', () => {
  it('pagesSelector должен вернуть pages', () => {
    expect(pagesSelector(state)).toEqual(state.pages);
  });
  it('makePageByIdSelector должен вернуть page по id', () => {
    expect(makePageByIdSelector('_')(state)).toEqual(state.pages._);
  });
  it('makePageMetadataByIdSelector должен вернуть page по id', () => {
    expect(makePageMetadataByIdSelector('_')(state)).toEqual(
      state.pages._.metadata
    );
  });
  it('makePageLoadingByIdSelector должен вернуть loading по id', () => {
    expect(makePageLoadingByIdSelector('_')(state)).toEqual(
      state.pages._.loading
    );
  });
  it('makePageRoutesByIdSelector должен вернуть routes по id', () => {
    expect(makePageRoutesByIdSelector('_')(state)).toEqual(
      state.pages._.metadata.routes
    );
  });
  it('makeWidgetMetadataSelector должен вернуть metadata виджета по id', () => {
    expect(makeWidgetMetadataSelector('_', 'widgetId')(state)).toEqual(
      state.pages._.metadata.widgets.widgetId
    );
  });
  it('makePageActionsByIdSelector должен вернуть actions по id', () => {
    expect(makePageActionsByIdSelector('_')(state)).toEqual(
      state.pages._.metadata.actions
    );
  });
  it('makePageToolbarByIdSelector должен вернуть toolbar по id', () => {
    expect(makePageToolbarByIdSelector('_')(state)).toEqual(
      state.pages._.metadata.toolbar
    );
  });
  it('makePageErrorByIdSelector должен вернуть error по id', () => {
    expect(makePageErrorByIdSelector('_')(state)).toEqual(state.pages._.error);
  });
  it('makePageTitleByIdSelector должен вернуть title по id', () => {
    expect(makePageTitleByIdSelector('_')(state)).toEqual(
      state.pages._.metadata.page.title
    );
  });
  it('makePageDisabledByIdSelector должен вернуть disabled по id', () => {
    expect(makePageDisabledByIdSelector('_')(state)).toEqual(
      state.pages._.disabled
    );
  });
});
