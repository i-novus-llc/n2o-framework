import { metadataSuccess } from '../../src/actions/pages';

import { store } from './utils';
import { changeRootPage } from '../../src/ducks/global/store';

const buildSimplePage = (widget, id) => {
  const widgetIds = Object.keys(widget);
  return {
    widgets: widget,
    id,
    layout: {
      src: 'SingleLayout',
      regions: {
        single: [
          {
            src: 'NoneRegion',
            items: widgetIds.map(widgetId => ({ widgetId })),
          },
        ],
      },
    },
  };
};

export default (widget, id = 'Page') => storyFn => {
  store.dispatch(metadataSuccess(id, buildSimplePage(widget)));
  store.dispatch(changeRootPage(id));
  return storyFn();
};
