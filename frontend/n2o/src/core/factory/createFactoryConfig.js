import merge from 'deepmerge';

// import pages from '../../components/pages';
import controls from '../../components/controls';
import widgets from '../../components/widgets';
import regions from '../../components/regions';
import headers from '../../components/widgets/Table/headers';
import cells from '../../components/widgets/Table/cells';
import fieldsets from '../../components/widgets/Form/fieldsets';
import fields from '../../components/widgets/Form/fields';
import actions from '../../impl/actions';
import snippets from '../../components/snippets';

export const factories = {
  // pages,
  controls,
  widgets,
  regions,
  headers,
  cells,
  fieldsets,
  fields,
  actions,
  snippets,
};

export default function createFactoryConfig(customConfig) {
  return merge(factories, customConfig);
}
