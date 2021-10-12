package games.nautilus.core.rank.command;

import games.nautilus.core.Nautilus;
import games.nautilus.core.rank.Rank;
import games.nautilus.core.user.User;
import games.nautilus.core.utils.CC;
import games.nautilus.core.utils.ItemBuilder;
import games.nautilus.core.utils.framework.command.Command;
import games.nautilus.core.utils.framework.command.CommandArgs;
import games.nautilus.core.utils.framework.command.Completer;
import games.nautilus.core.utils.framework.menu.Button;
import games.nautilus.core.utils.framework.menu.Menu;
import games.nautilus.core.utils.framework.menu.ShapedMenuPattern;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankCommand {

    private final Nautilus core;

    public RankCommand(@NotNull Nautilus core) {
        this.core = core;
        core.getFramework().registerCommands(this);
    }

    @Command(name = "rank", permission = "nautilus.rank")
    public void rankCommand(CommandArgs args) {
        core.getUserManager().getUserFromId(args.getPlayer().getUniqueId()).ifPresent(user -> {
            getRankMenu(user, 0).open(user.getPlayer());
        });
    }

    @Completer(name = "rank")
    public List<String> rankCompleter(CommandArgs args) {
        return Collections.emptyList();
    }

    private @NotNull Menu getRankMenu(User user, int page) {
        List<Rank> ranks = core.getRankManager().getRanksPriorityOrder();
        int[] indexes = new int[] { 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43 };

        List<Rank> ranksInPage = new ArrayList<>();
        for (int i = page * indexes.length; i < (Math.min(ranks.size() - (page * indexes.length), ((page * indexes.length) + indexes.length))); i++) {
            ranksInPage.add(ranks.get(i));
        }
        System.out.println(ranksInPage.size());
        System.out.println(indexes.length);

        Menu menu = new Menu(core, "Rank Editor", 6);
        ShapedMenuPattern pattern = new ShapedMenuPattern(
                new char[] {'#','#','#','#','#','#','#','#','#'},
                new char[] {'#',' ',' ','H',' ','I',' ',' ','#'},
                new char[] {'#','#','#','#','#','#','#','#','#'},
                new char[] {'#',' ',' ',' ',' ',' ',' ',' ','#'},
                new char[] {'#',' ',' ',' ',' ',' ',' ',' ','#'},
                new char[] {'P','#','#','#','#','#','#','#','N'}
        );

        pattern.set('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        pattern.set('H', new ItemBuilder(Material.PLAYER_HEAD, 3).setSkull(user.getPlayer().getName())
                .name(user.getPrimaryRank().getChatPrefix() + user.getPlayer().getName()).build());
        pattern.set('I', new ItemBuilder(Material.BOOK).name("&b&l(!) &bInformation").lore(
                "&7Here you can select a rank to modify",
                "&7Click on the rank you wish to modify to open the menu"
        ).build());

        if (page == 0) {
            pattern.set('P', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        } else {
            pattern.set('P', new Button() {
                @Override
                public void clicked(Player player) {
                    core.getUserManager().getUserFromId(player.getUniqueId()).ifPresent(user -> {
                        menu.close(); getRankMenu(user, page - 1).open(player);
                    });
                }
            }.setIcon(new ItemBuilder(Material.ARROW).name("&bPrevious").build()));
        }

        if (ranksInPage.size() < 14) {
            pattern.set('N', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        } else {
            pattern.set('N', new Button() {
                @Override
                public void clicked(Player player) {
                    core.getUserManager().getUserFromId(player.getUniqueId()).ifPresent(user -> {
                        menu.close(); getRankMenu(user, page + 1).open(player);
                    });
                }
            }.setIcon(new ItemBuilder(Material.ARROW).name("&bNext").build()));
        }

        menu.applyMenuPattern(pattern);

        for (int i = 0; i < ranksInPage.size(); i++) {
            Rank r = ranksInPage.get(i);
            ItemStack icon = new ItemBuilder(Material.LEATHER_CHESTPLATE).name(r.getDisplayName()).lore("&7Click to edit this rank!").build();
            LeatherArmorMeta meta = (LeatherArmorMeta) icon.getItemMeta();
            meta.setColor(CC.getColor(CC.getLastColor(r.getDisplayName())));
            icon.setItemMeta(meta);

            menu.setButton(indexes[i], new Button() {
                @Override
                public void clicked(Player player) {

                }
            }.setIcon(icon));
        }

        return menu.build();
    }

}
