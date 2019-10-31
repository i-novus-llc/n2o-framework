import { compose, withHandlers, defaultProps } from 'recompose';
import CheckboxN2O from './CheckboxN2O';

export default compose(
  defaultProps({
    defaultUnchecked: false,
  }),
  withHandlers({
    onChange: ({ defaultUnchecked, ...props }) => event => {
      const value = event.nativeEvent.target.checked;

      props.onChange(!value ? defaultUnchecked : value);
    },
  })
)(CheckboxN2O);
