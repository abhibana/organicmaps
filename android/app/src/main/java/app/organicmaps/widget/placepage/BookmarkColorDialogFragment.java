package app.organicmaps.widget.placepage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import app.organicmaps.R;
import app.organicmaps.base.BaseMwmDialogFragment;
import app.organicmaps.bookmarks.ColorsAdapter;
import app.organicmaps.sdk.bookmarks.data.PredefinedColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;

public class BookmarkColorDialogFragment extends BaseMwmDialogFragment
{
  public static final String ICON_COLOR = "ExtraIconColor";
  public static final String ICON_RES = "ExtraIconRes";

  @PredefinedColors.Color
  private int mIconColor;

  @DrawableRes
  private int mIconResId = app.organicmaps.sdk.R.drawable.ic_bookmark_none;

  public interface OnBookmarkColorChangeListener
  {
    void onBookmarkColorSet(@PredefinedColors.Color int color);
  }

  private OnBookmarkColorChangeListener mColorSetListener;

  public BookmarkColorDialogFragment() {}

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    if (getArguments() != null)
    {
      if (getArguments().containsKey(ICON_COLOR))
        mIconColor = getArguments().getInt(ICON_COLOR);
      if (getArguments().containsKey(ICON_RES))
        mIconResId = getArguments().getInt(ICON_RES);
    }

    return new MaterialAlertDialogBuilder(requireActivity(), R.style.MwmTheme_AlertDialog)
        .setView(buildView())
        .setTitle(R.string.choose_color)
        .setNegativeButton(R.string.cancel, null)
        .create();
  }

  public void setOnColorSetListener(OnBookmarkColorChangeListener listener)
  {
    mColorSetListener = listener;
  }

  @NonNull
  private View buildView()
  {
    final List<Integer> colors = PredefinedColors.getAllPredefinedColors();
    final ColorsAdapter adapter = new ColorsAdapter(requireActivity(), colors, mIconResId);
    adapter.chooseItem(mIconColor);

    @SuppressLint("InflateParams")
    final GridView gView =
        (GridView) LayoutInflater.from(requireActivity()).inflate(R.layout.fragment_color_grid, null);
    gView.setAdapter(adapter);
    gView.setOnItemClickListener((parent, view, pos, id) -> {
      if (mColorSetListener != null)
        mColorSetListener.onBookmarkColorSet(adapter.getItem(pos));
      dismiss();
    });

    return gView;
  }
}
