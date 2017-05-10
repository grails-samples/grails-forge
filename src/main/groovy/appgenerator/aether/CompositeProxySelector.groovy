package appgenerator.aether


import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;

/**
 * Composite {@link ProxySelector}.
 *
 * @author Dave Syer
 * @since 1.1.0
 */
public class CompositeProxySelector implements ProxySelector {

    private List<ProxySelector> selectors = new ArrayList<ProxySelector>();

    public CompositeProxySelector(List<ProxySelector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public Proxy getProxy(RemoteRepository repository) {
        for (ProxySelector selector : this.selectors) {
            Proxy proxy = selector.getProxy(repository);
            if (proxy != null) {
                return proxy;
            }
        }
        return null;
    }

}
