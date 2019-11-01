import actions from '../../impl/actions';
import exportModal from '../../components/widgets/Table/ExportModal';
import ToggleColumn from '../../components/actions/Dropdowns/ToggleColumn';
import ChangeSize from '../../components/actions/Dropdowns/ChangeSize';
import NotFoundFactory from './NotFoundFactory';

const index = {
  ...actions,
  exportModal,
  ToggleColumn,
  ChangeSize,
  NotFoundFactory,
};

const actionResolver = (src, { actions = {} }) => {
  const config = {
    ...index,
    ...actions,
  };

  return config[src] || NotFoundFactory;
};

export default actionResolver;
