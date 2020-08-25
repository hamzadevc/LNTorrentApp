package org.lntorrent.libretorrent.ui.detailtorrent.pages.peers;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.lntorrent.libretorrent.R;
import org.lntorrent.libretorrent.core.model.data.PeerInfo;
import org.lntorrent.libretorrent.core.utils.Utils;
import org.lntorrent.libretorrent.databinding.ItemPeersListBinding;
import org.lntorrent.libretorrent.ui.Selectable;

import java.util.Collections;
import java.util.List;

public class PeerListAdapter extends ListAdapter<PeerItem, PeerListAdapter.ViewHolder>
        implements Selectable<PeerItem>
{
    @SuppressWarnings("unused")
    private static final String TAG = PeerListAdapter.class.getSimpleName();

    private ClickListener listener;

    public PeerListAdapter(ClickListener listener)
    {
        super(diffCallback);

        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPeersListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_peers_list,
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.bind(getItem(position), listener);
    }

    @Override
    public void submitList(@Nullable List<PeerItem> list)
    {
        if (list != null)
            Collections.sort(list);

        super.submitList(list);
    }

    @Override
    public PeerItem getItemKey(int position)
    {
        if (position < 0 || position >= getCurrentList().size())
            return null;

        return getItem(position);
    }

    @Override
    public int getItemPosition(PeerItem key)
    {
        return getCurrentList().indexOf(key);
    }

    private static final DiffUtil.ItemCallback<PeerItem> diffCallback = new DiffUtil.ItemCallback<PeerItem>()
    {
        @Override
        public boolean areContentsTheSame(@NonNull PeerItem oldItem,
                                          @NonNull PeerItem newItem)
        {
            return oldItem.equalsContent(newItem);
        }

        @Override
        public boolean areItemsTheSame(@NonNull PeerItem oldItem,
                                       @NonNull PeerItem newItem)
        {
            return oldItem.equals(newItem);
        }
    };

    public interface ClickListener
    {
        boolean onItemLongClick(@NonNull PeerItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ItemPeersListBinding binding;

        public ViewHolder(ItemPeersListBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
            Utils.colorizeProgressBar(itemView.getContext(), binding.progress);
        }

        void bind(PeerItem item, ClickListener listener)
        {
            Context context = itemView.getContext();

            itemView.setOnLongClickListener((v) -> {
                if (listener == null)
                    return false;

                return listener.onItemLongClick(item);
            });

            binding.ip.setText(item.ip);
            binding.progress.setProgress(item.progress);

            binding.port.setText(context.getString(R.string.peer_port, item.port));

            binding.relevance.setText(context.getString(R.string.peer_relevance, item.relevance));

            String connectionType = "";
            switch (item.connectionType) {
                case PeerInfo.ConnectionType.BITTORRENT:
                    connectionType = context.getString(R.string.peer_connection_type_bittorrent);
                    break;
                case PeerInfo.ConnectionType.WEB:
                    connectionType = context.getString(R.string.peer_connection_type_web);
                    break;
                case PeerInfo.ConnectionType.UTP:
                    connectionType = context.getString(R.string.peer_connection_type_utp);
                    break;
            }
            binding.connectionType.setText(context.getString(R.string.peer_connection_type, connectionType));

            String downSpeed = Formatter.formatFileSize(context, item.downSpeed);
            String upSpeed = Formatter.formatFileSize(context, item.upSpeed);
            binding.upDownSpeed.setText(context.getString(R.string.download_upload_speed_template, downSpeed, upSpeed));

            binding.client.setText(context.getString(R.string.peer_client, item.client));

            String upload = Formatter.formatFileSize(context, item.totalUpload);
            String download = Formatter.formatFileSize(context, item.totalDownload);
            binding.totalDownloadUpload.setText(context.getString(R.string.peer_total_download_upload, download, upload));
        }
    }
}
