import React from 'react';
import PropTypes from 'prop-types';
import Wireframe from '../src/components/snippets/Wireframe/Wireframe';
import CoverSpinner from '../src/components/snippets/Spinner/CoverSpinner';

class WireframeTimeOut extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      message: 'Компонент не загрузился. Загружаем данные...',
    };
  }

  async componentDidMount() {
    const delay = timeout => new Promise(res => setTimeout(res, timeout));

    await delay(2000);
    await this.setState({ message: 'Еще чуть чуть...' });
    await delay(1000);
    await this.setState({ loading: false });
  }

  render() {
    const { loading, message } = this.state;

    return loading ? <CoverSpinner message={message}/> : <Wireframe { ...this.props } />;
  }
}

WireframeTimeOut.propTypes = {
  className: PropTypes.string,
  title: PropTypes.string,
};

export default WireframeTimeOut;
